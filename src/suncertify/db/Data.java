package suncertify.db;

import static suncertify.util.Constants.UNDERSCORE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import suncertify.util.DataConverter;

// TODO: Auto-generated Javadoc
/**
 * The class Data is an implementation of {@link DBMain}. It reads the contents
 * of the non-relational database file into an in-memory cache when an instance
 * is created. All queries and updates to the database are then done to the
 * in-memory cache instead using the public methods declared in the
 * {@link DBMain} interface. It writes the contents of the cache back to the
 * database file when the application terminates. <br>
 * Uses the singleton pattern to ensure there is at most one instance at any one
 * time.
 */
public class Data implements DBMainExtended {

	/** The Constant INSTANCE. */
	private static final DBMainExtended INSTANCE = new Data();

	/** The in-memory cache containing the contents of the database file. */
	private Map<Integer, String[]> dbCache = new HashMap<>();

	/** The in-memory cache of records which have been locked by a thread. */
	private Set<Integer> lockedRecords = new HashSet<>();

	/**
	 * Used to perform read/write operations on the database file.
	 */
	private DBAccessManager dbAccessManager;

	/**
	 * Constructs a new default Data instance.
	 */
	private Data() {

	}

	/**
	 * Gets the single instance of DBMainExtended.
	 *
	 * @return single instance of DBMainExtended.
	 */
	public static DBMainExtended getInstance() {
		return INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void initialize(final String dbFileLocation) throws DatabaseException {
		this.dbAccessManager = DBFileAccessManager.getInstance();
		dbAccessManager.initialize(dbFileLocation);
		load();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				save();
			} catch (DatabaseException e) {
				System.err.println("Could not save data: " + e.getMessage());
			}
		}));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized String[] read(final int recNo) throws RecordNotFoundException {
		if (isInvalidRecord(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid record");
		}
		return dbCache.get(recNo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void update(final int recNo, final String[] data) {
		validateFieldLengths(data);
		dbCache.put(recNo, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void delete(final int recNo) {
		dbCache.put(recNo, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized int[] find(final String[] criteria) throws RecordNotFoundException {
		validateFieldLengths(criteria);
		final List<Integer> recordNumberList = new ArrayList<>();
		findAllValidRecords().forEach((recordNumber, fieldValues) -> {
			boolean isMatch = true;
			for (int index = 0; index < criteria.length; index++) {
				final String recordValue = fieldValues[index].toUpperCase();
				final String searchValue = criteria[index].toUpperCase();
				if (!recordValue.startsWith(searchValue)) {
					isMatch = false;
					break;
				}
			}
			if (isMatch) {
				recordNumberList.add(recordNumber);
			}
		});

		if (recordNumberList.isEmpty()) {
			throw new RecordNotFoundException(
					"No matching records for selected criteria: Name=" + criteria[0] + " , Location=" + criteria[1]);
		}
		final int[] matchingRecordNumbers = DataConverter.convertIntegerListToIntArray(recordNumberList);
		return matchingRecordNumbers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized int create(final String[] data) throws DuplicateKeyException {
		validateFieldLengths(data);
		final String newPrimaryKey = generatePrimaryKey(data);
		for (Entry<Integer, String[]> record : findAllValidRecords().entrySet()) {
			final Integer recordNumber = record.getKey();
			final String[] fieldValues = record.getValue();
			final String existingPrimaryKey = generatePrimaryKey(fieldValues);
			if (newPrimaryKey.equalsIgnoreCase(existingPrimaryKey)) {
				throw new DuplicateKeyException("Record already exists! See record " + recordNumber);
			}
		}
		final int recordNumber = generateRecordNumber();
		dbCache.put(new Integer(recordNumber), data);
		return recordNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void lock(final int recNo) throws RecordNotFoundException {
		while (isLocked(recNo)) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
		lockedRecords.add(recNo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void unlock(final int recNo) {
		if (lockedRecords.contains(recNo)) {
			lockedRecords.remove(recNo);
			notifyAll();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean isLocked(final int recNo) throws RecordNotFoundException {
		if (isInvalidRecord(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid record");
		}
		return lockedRecords.contains(recNo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void save() throws DatabaseException {
		dbAccessManager.persist(dbCache);
	}

	/**
	 * Compares the size of the each element in the specified
	 * {@code fieldValues} against the max permitted field size described in the
	 * schema description section of the database file for that field. If the
	 * number of characters used in a field exceeds the max number of characters
	 * permitted for that field, a {@link IllegalArgumentException} will be
	 * thrown.
	 *
	 * @param fields
	 *            the fields
	 * @throws IllegalArgumentException
	 *             if the number of characters used in a field exceeds the max
	 *             number of characters permitted for that field
	 */
	private void validateFieldLengths(final String[] fields) {
		dbAccessManager.validateFieldsAgainstSchema(fields);
	}

	/**
	 * Generates the record number. If the database contains any deleted
	 * records, this method will return the lowest record number of the deleted
	 * records, thereby reusing any existing record numbers of deleted records.
	 * Otherwise, it will return {@code dbCache.size()}
	 *
	 * @return the record number
	 */
	private int generateRecordNumber() {
		int deletedRecordNumber = dbCache.size();
		for (Entry<Integer, String[]> record : dbCache.entrySet()) {
			if (record.getValue() == null) {
				deletedRecordNumber = record.getKey();
				break;
			}
		}
		return deletedRecordNumber;
	}

	/**
	 * Generates a primary key from the String[] argument using the first two
	 * elements in the String array, i.e. the name of the contractor and their
	 * location.
	 *
	 * @param data
	 *            the data
	 * @return the primary key in String form
	 */
	private String generatePrimaryKey(final String[] data) {
		final String name = data[0];
		final String location = data[1];
		final String uniqueKey = name + UNDERSCORE + location;
		return uniqueKey;
	}

	/**
	 * Checks if a specified record is invalid. Returns true if the specified
	 * record does not exist or is marked as deleted in the database. Otherwise,
	 * returns false;
	 *
	 * @param recNo
	 *            the record number
	 * @return true, if the specified record does not exist or is marked as
	 *         deleted in the database
	 */
	private boolean isInvalidRecord(final int recNo) {
		return !findAllValidRecords().containsKey(recNo);
	}

	/**
	 * Returns a map containing all records in the database which are not marked
	 * as deleted.
	 *
	 * @return a map containing all the valid record numbers as the map keys and
	 *         their corresponding records as the map values
	 */
	private Map<Integer, String[]> findAllValidRecords() {
		final Map<Integer, String[]> validRecords = new HashMap<>();
		dbCache.forEach((recordNumber, fieldValues) -> {
			if (fieldValues != null) {
				validRecords.put(new Integer(recordNumber), fieldValues);
			}
		});
		return validRecords;
	}

	// TODO: DELETE TEST METHODS BELOW BEFORE SUBMISSION

	/**
	 * Gets the total number of records.
	 *
	 * @return the total number of records
	 */
	public int getTotalNumberOfRecords() {
		return dbCache.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public void load() throws DatabaseException {
		dbAccessManager.readDatabaseIntoCache(dbCache);
	}

	/**
	 * Clear.
	 */
	public synchronized void clear() {
		dbCache.clear();
	}
}
