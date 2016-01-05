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

/**
 * The class Data is an implementation of {@link DBMain}. It reads the contents
 * of the non-relational database file into an in-memory cache when an instance
 * is created. All queries and updates to the database are then done to the
 * in-memory cache instead using the public methods declared in the
 * {@link DBMain} interface. It writes the contents of the cache back to the
 * database file when the application terminates
 */
public class Data implements DBMain {

	/** The in-memory cache containing the contents of the database file. */
	private final Map<Integer, String[]> dbCache = new HashMap<>();

	/** The in-memory cache of records which have been locked by a thread. */
	private final Set<Integer> lockedRecords = new HashSet<>();

	/**
	 * Used to perform read/write operations on the database file.
	 */
	private final DBFileAccessManager dbFileAccessManager;

	/**
	 * Constructs a new Data instance with a specified location of the database
	 * file in the file system. The constructor is responsible for instantiating
	 * the {@code dBFileAccessManager}, loading the contents of the database
	 * file into the {@code dbCache} and ensuring that the contents of the
	 * {@code dbCache} is written back to the database file when the application
	 * is shut down.
	 *
	 * @param dbFileLocation
	 *            the location of the database on the file system
	 * @throws DatabaseException
	 *             the database exception
	 */
	public Data(final String dbFileLocation) throws DatabaseException {
		this.dbFileAccessManager = new DBFileAccessManager(dbFileLocation);
		dbFileAccessManager.readDatabaseIntoCache(dbCache);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				dbFileAccessManager.persist(dbCache);
			} catch (DatabaseException e) {
				System.err.println("Could not save data: " + e.getMessage());
			}
		}));
	}

	/**
	 * Reads a record from the file. Returns an array where each element is a
	 * record value
	 *
	 * @param recNo
	 *            the record number
	 * @return a string array where each element is a record value
	 * @throws RecordNotFoundException
	 *             If the specified record does not exist or is marked as
	 *             deleted in the database
	 */
	@Override
	public synchronized String[] read(final int recNo) throws RecordNotFoundException {
		if (isInvalidRecord(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid record");
		}
		return dbCache.get(recNo);
	}

	/**
	 * Modifies the fields of a record. The new value for field n appears in
	 * data[n].
	 *
	 * @param recNo
	 *            the record number
	 * @param data
	 *            a string array where each element is a record value
	 * @throws RecordNotFoundException
	 *             If the specified record does not exist or is marked as
	 *             deleted in the database
	 */
	@Override
	public synchronized void update(final int recNo, final String[] data) throws RecordNotFoundException {
		if (isInvalidRecord(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid record");
		}
		dbCache.put(recNo, data);
	}

	/**
	 * Deletes a record, making the record number and associated disk storage
	 * available for reuse.
	 *
	 * @param recNo
	 *            the record number
	 * @throws RecordNotFoundException
	 *             If the specified record does not exist or is marked as
	 *             deleted in the database
	 */
	@Override
	public synchronized void delete(final int recNo) throws RecordNotFoundException {
		if (isInvalidRecord(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid record");
		}
		dbCache.put(recNo, null);
	}

	/**
	 * Returns an array of record numbers that match the specified criteria.
	 * Field n in the database file is described by criteria[n]. A null value in
	 * criteria[n] matches any field value. A non-null value in criteria[n]
	 * matches any field value that begins with criteria[n]. (For example,
	 * "Fred" matches "Fred" or "Freddy".)
	 *
	 * @param criteria
	 *            the criteria
	 * @return an array of record numbers that match the specified criteria
	 * @throws RecordNotFoundException
	 *             If the specified record does not exist or is marked as
	 *             deleted in the database
	 */
	@Override
	public synchronized int[] find(final String[] criteria) throws RecordNotFoundException {
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
	 * Creates a new record in the database (possibly reusing a deleted entry).
	 * Inserts the given data, and returns the record number of the new record.
	 *
	 * @param data
	 *            a string array where each element is a record value
	 * @return the record number of the new record
	 * @throws DuplicateKeyException
	 *             If an existing record in the database, which has not been
	 *             marked as deleted, contains the same key specified in the
	 *             given data
	 */
	@Override
	public synchronized int create(final String[] data) throws DuplicateKeyException {
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
	 * Locks a record so that it can only be updated or deleted by this client.
	 * If the specified record is already locked, the current thread gives up
	 * the CPU and consumes no CPU cycles until the record is unlocked.
	 *
	 * @param recNo
	 *            the record number
	 * @throws RecordNotFoundException
	 *             If the specified record does not exist or is marked as
	 *             deleted in the database
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
	 * Releases the lock on a record.
	 *
	 * @param recNo
	 *            the record number
	 * @throws RecordNotFoundException
	 *             If the specified record does not exist or is marked as
	 *             deleted in the database
	 */
	@Override
	public synchronized void unlock(final int recNo) throws RecordNotFoundException {
		if (lockedRecords.contains(recNo)) {
			lockedRecords.remove(recNo);
			notifyAll();
		} else {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid locked record");
		}
	}

	/**
	 * Determines if a record is currently locked. Returns true if the record is
	 * locked, false otherwise.
	 *
	 * @param recNo
	 *            the record number
	 * @return true, if is locked
	 * @throws RecordNotFoundException
	 *             If the specified record does not exist or is marked as
	 *             deleted in the database
	 */
	@Override
	public synchronized boolean isLocked(final int recNo) throws RecordNotFoundException {
		if (isInvalidRecord(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid record");
		}
		return lockedRecords.contains(recNo);
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
	 * Clear.
	 */
	public synchronized void clear() {
		dbCache.clear();
	}

	/**
	 * Save.
	 *
	 * @throws DatabaseException
	 *             the database exception
	 */
	public synchronized void save() throws DatabaseException {
		dbFileAccessManager.persist(dbCache);
	}

	/**
	 * Load.
	 *
	 * @throws DatabaseException
	 *             the database exception
	 */
	public synchronized void load() throws DatabaseException {
		dbFileAccessManager.readDatabaseIntoCache(dbCache);
	}
}
