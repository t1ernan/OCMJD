package suncertify.db;

import static suncertify.util.Constants.EMPTY_STRING;
import static suncertify.util.Constants.RECORD_FIELDS;
import static suncertify.util.Constants.SEARCH_FIELDS;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public final class Data implements DBMainExtended {

	private static final Data INSTANCE = new Data();
	private static final int MAGIC_COOKIE = 514;
	private static final String[] FIELD_NAMES = { "name", "location", "specialties", "size", "rate", "owner" };
	private static final int[] MAX_FIELD_SIZES = { 32, 64, 64, 6, 8, 8 };
	private static final int VALID_FLAG = 00;
	private static final int DELETED_FLAG = 0x8000;
	private static final Logger LOGGER = Logger.getLogger(Data.class.getName());
	private final Map<Integer, String[]> cache = new HashMap<>();
	private final Set<Integer> lockedRecords = new HashSet<>();
	private String dbFileLocation;
	private int recordOffset;

	private Data() {
	}

	public static Data getInstance() {
		return INSTANCE;
	}

	public synchronized void clear() {
		cache.clear();
	}

	@Override
	public synchronized int create(final String[] data) throws DuplicateKeyException {
		validateFields(data);
		checkForDuplicateKey(data);
		final OptionalInt recordNumberOptional = getDeletedRecordsStream().mapToInt(entry -> entry.getKey()).findAny();
		final int recordNumber = recordNumberOptional.orElse(cache.size());
		cache.put(recordNumber, data);
		return recordNumber;
	}

	@Override
	public synchronized void delete(final int recNo) {
		cache.put(recNo, null);
	}

	@Override
	public synchronized int[] find(final String[] criteria) throws RecordNotFoundException {
		validateFields(criteria);
		if (criteria.length != SEARCH_FIELDS) {
			throw new IllegalArgumentException("Search criteria should contain Name and Location values only!");
		}
		final int[] matchingRecordNumbers = getValidRecordsStream()
				.filter(entry -> doesEntryMatchCriteria(entry.getValue(), criteria)).mapToInt(entry -> entry.getKey())
				.toArray();
		if (matchingRecordNumbers.length == 0) {
			throw new RecordNotFoundException(
					"No matching records for selected criteria: Name=" + criteria[0] + " , Location=" + criteria[1]);
		}
		return matchingRecordNumbers;
	}

	public Map<Integer, String[]> getAllValidRecords() {
		final Map<Integer, String[]> validRecords = new HashMap<>();
		cache.forEach((recordNumber, fieldValues) -> {
			if (fieldValues != null) {
				validRecords.put(recordNumber, fieldValues);
			}
		});
		return validRecords;
	}

	public int getRecordNumber() {
		int deletedRecordNumber = cache.size();
		for (final Entry<Integer, String[]> record : cache.entrySet()) {
			if (record.getValue() == null) {
				deletedRecordNumber = record.getKey();
				break;
			}
		}
		return deletedRecordNumber;
	}

	public int getTotalNumberOfRecords() {
		return cache.size();
	}

	@Override
	public synchronized void initialize(final String dbFileLocation) throws DatabaseException {
		try (RandomAccessFile raf = new RandomAccessFile(dbFileLocation, "rwd")) {
			if (raf.readInt() != MAGIC_COOKIE) {
				throw new DatabaseException("Invalid file. Unexpected magic cookie value");
			}
			this.dbFileLocation = dbFileLocation;
			recordOffset = raf.readInt();
			loadCache();
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					save();
				} catch (final IOException e) {
					if (LOGGER.isLoggable(Level.WARNING)) {
						LOGGER.warning("Could not save data: " + e.getMessage());
					}
				}
			}));
		} catch (final IOException e) {
			throw new DatabaseException("Could not read data from the specified file: " + dbFileLocation, e);
		}
	}

	@Override
	public synchronized boolean isLocked(final int recNo) throws RecordNotFoundException {
		if (isInvalidRecord(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid record");
		}
		return lockedRecords.contains(recNo);
	}

	public synchronized void load() throws IOException {
		loadCache();
	}

	@Override
	public synchronized void lock(final int recNo) throws RecordNotFoundException {
		while (isLocked(recNo)) {
			try {
				wait();
			} catch (final InterruptedException e) {
				LOGGER.warning(e.getMessage());
			}
		}
		lockedRecords.add(recNo);
	}

	@Override
	public synchronized String[] read(final int recNo) throws RecordNotFoundException {
		if (isInvalidRecord(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid record");
		}
		return cache.get(recNo);
	}

	@Override
	public synchronized void save() throws IOException {
		try (RandomAccessFile raf = new RandomAccessFile(dbFileLocation, "rwd")) {
			raf.seek(recordOffset);
			raf.setLength(recordOffset);
			for (final String[] fieldValues : cache.values()) {
				writeRecord(raf, fieldValues);
			}
		}
	}

	@Override
	public synchronized void unlock(final int recNo) {
		if (lockedRecords.contains(recNo)) {
			lockedRecords.remove(recNo);
			notifyAll();
		}
	}

	@Override
	public synchronized void update(final int recNo, final String[] data) {
		validateFields(data);
		cache.put(recNo, data);
	}

	private void checkForDuplicateKey(final String[] data) throws DuplicateKeyException {
		final String newId = getUniqueId(data);
		final boolean isDuplicate = getValidRecordsStream().map(entry -> entry.getValue())
				.anyMatch(values -> newId.equals(getUniqueId(values)));
		if (isDuplicate) {
			throw new DuplicateKeyException("Record already exists");
		}
	}

	/**
	 * Does entry match criteria.
	 *
	 * @param fieldValues
	 *            the field values
	 * @param searchValues
	 *            the search values
	 * @return true, if successful
	 */
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

	private Stream<Entry<Integer, String[]>> getDeletedRecordsStream() {
		return cache.entrySet().stream().filter(entry -> entry.getValue() == null);
	}

	private String getUniqueId(final String[] fieldValues) {
		final String name = fieldValues[0].toUpperCase();
		final String location = fieldValues[1].toUpperCase();
		return (name + location);
	}

	private Stream<Entry<Integer, String[]>> getValidRecordsStream() {
		return cache.entrySet().stream().filter(entry -> entry.getValue() != null);
	}

	// TODO: DELETE TEST METHODS BELOW BEFORE SUBMISSION

	private boolean isInvalidRecord(final int recNo) {
		return getValidRecordsStream().noneMatch(entry -> entry.getKey() == recNo);
	}

	private void loadCache() throws IOException {
		try (RandomAccessFile raf = new RandomAccessFile(dbFileLocation, "rwd")) {
			int recordNumber = 0;
			raf.seek(recordOffset);
			while (raf.getFilePointer() < raf.length()) {
				cache.put(recordNumber, readFieldValues(raf));
				recordNumber++;
			}
		}
	}

	private String[] readFieldValues(final RandomAccessFile raf) throws IOException {
		final int flagvalue = raf.readUnsignedShort();
		String[] fieldValues = new String[RECORD_FIELDS];
		for (int index = 0; index < RECORD_FIELDS; index++) {
			final String fieldValue = readString(raf, MAX_FIELD_SIZES[index]);
			fieldValues[index] = fieldValue;
		}
		if (flagvalue != VALID_FLAG) {
			fieldValues = null;
		}
		return fieldValues;

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
			final int maxFieldSize = MAX_FIELD_SIZES[index];
			if (fieldSize > maxFieldSize) {
				throw new IllegalArgumentException("'" + fieldValues[index]
						+ "' exceeds the maximum number of characters permitted for the field '" + FIELD_NAMES[index]
								+ ". Max permitted characters: " + maxFieldSize);
			}
		}
	}

	/**
	 * Writes a record to the specified {@code dbFile}. The record will be have
	 * the field values specified in the elements of the {@code fieldValues}
	 * string array. If the specified {@code fieldValues} argument is null, the
	 * record will be filled with blank space ASCII characters.
	 *
	 * @param raf
	 *            a {@link RandomAccessFile} file for reading and writing to the
	 *            database file.
	 * @param fieldValues
	 *            a string array where each element is a record value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void writeRecord(final RandomAccessFile raf, final String[] fieldValues) throws IOException {
		if (fieldValues != null) {
			raf.writeShort(VALID_FLAG);
			for (int index = 0; index < RECORD_FIELDS; index++) {
				writeString(raf, fieldValues[index], MAX_FIELD_SIZES[index]);
			}
		} else {
			raf.writeShort(DELETED_FLAG);
			for (int index = 0; index < RECORD_FIELDS; index++) {
				writeString(raf, EMPTY_STRING, MAX_FIELD_SIZES[index]);
			}
		}
	}
}
