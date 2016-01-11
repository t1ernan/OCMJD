package suncertify.db;

import static suncertify.util.Constants.EMPTY_STRING;
import static suncertify.util.Constants.PRIMARY_KEY_FIELDS;
import static suncertify.util.Utils.readString;
import static suncertify.util.Utils.writeString;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Stream;

public class Data2 implements DBMainExtended {

	private final static Data2 INSTANCE = new Data2();
	private final int EXPECTED_MAGIC_COOKIE = 514;
	private int recordOffset;
	private final int NUMBER_OF_FIELDS = 6;
	private final String[] fieldNames = { "name", "location", "specialties", "size", "rate", "owner" };
	private final int[] maxFieldSizes = { 32, 64, 64, 6, 8, 8 };
	private Map<Integer, String[]> cache = new HashMap<>();
	private Set<Integer> lockedRecords = new HashSet<>();
	private String dbFileLocation = null;
	public static final int VALID_FLAG = 00;
	public static final int DELETED_FLAG = 0x8000;

	private Data2() {

	}

	public static Data2 getInstance() {
		return INSTANCE;
	}

	@Override
	public synchronized void initialize(final String dbFileLocation) throws DatabaseException {
		try {
			this.dbFileLocation = dbFileLocation;
			this.recordOffset = readRecordOffset(dbFileLocation);
			loadCache();
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					save();
				} catch (IOException e) {
					System.err.println("Could not save data: " + e.getMessage());
				}
			}));
		} catch (IOException e) {
			throw new DatabaseException("Could not read data from the specified file: " + dbFileLocation, e);
		}

	}

	@Override
	public synchronized String[] read(int recNo) throws RecordNotFoundException {
		if (isInvalidRecord(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid record");
		}
		return cache.get(recNo);
	}

	@Override
	public synchronized void update(int recNo, String[] data) {
		validateFields(data);
		cache.put(recNo, data);
	}

	@Override
	public synchronized void delete(int recNo) {
		cache.put(recNo, null);

	}

	@Override
	public synchronized int[] find(String[] criteria) throws RecordNotFoundException {
		validateFields(criteria);
		if (criteria.length != PRIMARY_KEY_FIELDS) {
			throw new IllegalArgumentException("Search criteria should contain Name and Location values only!");
		}
		final int[] matchingRecordNumbers = getValidRecordStream()
				.filter(entry -> doesEntryMatchCriteria(entry.getValue(), criteria)).mapToInt(entry -> entry.getKey())
				.toArray();
		if (matchingRecordNumbers.length == 0) {
			throw new RecordNotFoundException(
					"No matching records for selected criteria: Name=" + criteria[0] + " , Location=" + criteria[1]);
		}
		return matchingRecordNumbers;
	}

	@Override
	public synchronized int create(String[] data) throws DuplicateKeyException {
		validateFields(data);
		checkForDuplicateKey(data);
		final OptionalInt recordNumberOptional = getDeletedRecordStream().mapToInt(entry -> entry.getKey()).findAny();
		final int recordNumber = recordNumberOptional.orElse(cache.size());
		cache.put(recordNumber, data);
		return recordNumber;
	}

	private synchronized void checkForDuplicateKey(String[] data) throws DuplicateKeyException {
		final String newId = getUniqueId(data);
		final boolean isDuplicate = getValidRecordStream().map(entry -> entry.getValue())
				.anyMatch(values -> newId.equals(getUniqueId(values)));
		if (isDuplicate) {
			throw new DuplicateKeyException("Record already exists");
		}
	}

	@Override
	public synchronized void lock(int recNo) throws RecordNotFoundException {
		while (isLocked(recNo)) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
		lockedRecords.add(recNo);

	}

	@Override
	public synchronized void unlock(int recNo) {
		if (lockedRecords.contains(recNo)) {
			lockedRecords.remove(recNo);
			notifyAll();
		}

	}

	@Override
	public synchronized boolean isLocked(int recNo) throws RecordNotFoundException {
		if (isInvalidRecord(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid record");
		}
		return lockedRecords.contains(recNo);
	}

	@Override
	public synchronized void save() throws IOException {
		try (RandomAccessFile dbFile = new RandomAccessFile(dbFileLocation, "rwd")) {
			dbFile.seek(recordOffset);
			dbFile.setLength(recordOffset);
			for (String[] fieldValues : cache.values()) {
				if (fieldValues == null) {
					writeNullRecord(dbFile);
				} else {
					writeRecord(dbFile, fieldValues);
				}
			}
		}
	}

	private synchronized void loadCache() throws IOException {
		try (RandomAccessFile dbFile = new RandomAccessFile(dbFileLocation, "rwd")) {
			int recordNumber = 0;
			dbFile.seek(recordOffset);
			while (dbFile.getFilePointer() < dbFile.length()) {
				final int flagvalue = dbFile.readUnsignedShort();
				final String[] fieldValues = readRecord(dbFile);
				if (flagvalue == VALID_FLAG) {
					cache.put(recordNumber, fieldValues);
				} else {
					cache.put(recordNumber, null);
				}
				recordNumber++;
			}
		}
	}

	private String[] readRecord(final RandomAccessFile raf) throws IOException {
		final String[] fieldValues = new String[NUMBER_OF_FIELDS];
		for (int index = 0; index < NUMBER_OF_FIELDS; index++) {
			final String fieldValue = readString(raf, maxFieldSizes[index]);
			fieldValues[index] = fieldValue;
		}
		return fieldValues;

	}

	/**
	 * Writes a null record to the specified {@code dbFile}. The record will be
	 * filled with blank space ASCII characters.
	 *
	 * @param dbFile
	 *            a {@link RandomAccessFile} file for reading and writing to the
	 *            database file.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void writeNullRecord(final RandomAccessFile dbFile) throws IOException {
		dbFile.writeShort(DELETED_FLAG);
		for (int index = 0; index < NUMBER_OF_FIELDS; index++) {
			writeString(dbFile, EMPTY_STRING, maxFieldSizes[index]);
		}
	}

	/**
	 * Writes a record to the specified {@code dbFile}. The record will be have
	 * the field values specified in the elements of the {@code fieldValues}
	 * string array.
	 *
	 * @param dbFile
	 *            a {@link RandomAccessFile} file for reading and writing to the
	 *            database file.
	 * @param fieldValues
	 *            a string array where each element is a record value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void writeRecord(final RandomAccessFile dbFile, final String[] fieldValues) throws IOException {
		dbFile.writeShort(VALID_FLAG);
		for (int index = 0; index < NUMBER_OF_FIELDS; index++) {
			writeString(dbFile, fieldValues[index], maxFieldSizes[index]);
		}
	}

	/**
	 * Compares the size of the each element in the specified
	 * {@code fieldValues} against the max permitted field size described in the
	 * schema description section of the database file for that field. If the
	 * number of characters used in a field exceeds the max number of characters
	 * permitted for that field, a {@link IllegalArgumentException} will be
	 * thrown.
	 *
	 * @param fieldValues
	 *            a string array where each element is a record value.
	 * @throws IllegalArgumentException
	 *             if the number of characters used in a field exceeds the max
	 *             number of characters permitted for that field
	 */
	private void validateFields(final String[] fieldValues) {
		for (int index = 0; index < fieldValues.length; index++) {
			final int fieldSize = fieldValues[index].length();
			final int maxFieldSize = maxFieldSizes[index];
			if (fieldSize > maxFieldSize) {
				throw new IllegalArgumentException("'" + fieldValues[index]
						+ "' exceeds the maximum number of characters permitted for the field '" + fieldNames[index]
						+ ". Max permitted characters: " + maxFieldSize);
			}
		}
	}

	private int readRecordOffset(final String dbFileLocation) throws DatabaseException, IOException {
		int recordOffset = 0;
		try (RandomAccessFile dbFile = new RandomAccessFile(dbFileLocation, "rwd")) {
			final int magicCookieValue = dbFile.readInt();
			if (magicCookieValue != EXPECTED_MAGIC_COOKIE) {
				throw new DatabaseException(
						"Invalid Database File. Magic Cookie value doesn't match expected value: " + magicCookieValue);
			}
			recordOffset = dbFile.readInt();
		}
		return recordOffset;
	}

	private boolean isInvalidRecord(int recNo) {
		return getValidRecordStream().noneMatch(entry -> entry.getKey() == recNo);
	}

	private Stream<Entry<Integer, String[]>> getValidRecordStream() {
		return cache.entrySet().stream().filter(entry -> entry.getValue() != null);
	}

	private Stream<Entry<Integer, String[]>> getDeletedRecordStream() {
		return cache.entrySet().stream().filter(entry -> entry.getValue() == null);
	}

	private boolean doesEntryMatchCriteria(final String[] fieldValues, final String[] searchValues) {
		boolean isMatch = true;
		for (int index = 0; index < searchValues.length; index++) {
			final String fieldValue = fieldValues[index].toUpperCase();
			final String searchValue = searchValues[index].toUpperCase();
			if (!fieldValue.startsWith(searchValue)) {
				isMatch = false;
				break;
			}
		}
		return isMatch;
	}

	private String getUniqueId(final String[] fieldValues) {
		final String name = fieldValues[0].toUpperCase();
		final String location = fieldValues[1].toUpperCase();
		final String uniqueIdentifier = name + location;
		return uniqueIdentifier;
	}

	// TODO: DELETE TEST METHODS BELOW BEFORE SUBMISSION

	public int getTotalNumberOfRecords() {
		return cache.size();
	}

	public synchronized void clear() {
		cache.clear();
	}

	public synchronized void load() throws IOException {
		this.loadCache();
	}

	public int getRecordNumber() {
		int deletedRecordNumber = cache.size();
		for (Entry<Integer, String[]> record : cache.entrySet()) {
			if (record.getValue() == null) {
				deletedRecordNumber = record.getKey();
				break;
			}
		}
		return deletedRecordNumber;
	}

	public Map<Integer, String[]> getAllValidRecords() {
		final Map<Integer, String[]> validRecords = new HashMap<>();
		cache.forEach((recordNumber, fieldValues) -> {
			if (fieldValues != null) {
				validRecords.put(new Integer(recordNumber), fieldValues);
			}
		});
		return validRecords;
	}
}
