package suncertify.db;

import static suncertify.util.Constants.UNDERSCORE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import suncertify.util.ListConverter;

public class Data implements DBMain {

	private final Map<Integer, String[]> cache = new HashMap<>();
	private final DBAccessManager dbAccessManager;
	private final Set<Integer> lockedRecords = new HashSet<>();

	public Data(String databasePath) throws DatabaseException {
		this.dbAccessManager = new DBAccessManager(databasePath);
		dbAccessManager.read(cache);

		// Runtime.getRuntime().addShutdownHook(new Thread() {
		// @Override
		// public void run() {
		// try {
		// dbAccessManager.persist(cache);
		// } catch (DatabaseException e) {
		// System.err.println("Could not save data: " + e.getMessage());
		// }
		// }
		// });
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
	public synchronized String[] read(int recNo) throws RecordNotFoundException {
		if (isInvalidRecord(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid record");
		}
		return cache.get(recNo);
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
	public synchronized void update(int recNo, String[] data) throws RecordNotFoundException {
		if (isInvalidRecord(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid record");
		}
		cache.put(recNo, data);
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
	public synchronized void delete(int recNo) throws RecordNotFoundException {
		if (isInvalidRecord(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid record");
		}
		cache.put(recNo, null);
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
	public synchronized int[] find(String[] criteria) throws RecordNotFoundException {
		final List<Integer> recordNumberList = new ArrayList<>();
		for (Entry<Integer, String[]> record : getAllValidRecords().entrySet()) {
			boolean isMatch = true;
			final Integer recordNumber = record.getKey();
			final String[] fieldValues = record.getValue();
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
		}
		if (recordNumberList.isEmpty()) {
			throw new RecordNotFoundException(
					"No matching records for selected criteria: Name=" + criteria[0] + " , Location=" + criteria[1]);
		}
		final int[] matchingRecordNumbers = ListConverter.convertIntegerListToIntArray(recordNumberList);
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
	public synchronized int create(String[] data) throws DuplicateKeyException {
		final String newUniqueKey = getUniqueKey(data);
		for (Entry<Integer, String[]> record : getAllValidRecords().entrySet()) {
			final Integer recordNumber = record.getKey();
			final String[] fieldValues = record.getValue();
			final String existingUniqueKey = getUniqueKey(fieldValues);
			if (newUniqueKey.equalsIgnoreCase(existingUniqueKey)) {
				throw new DuplicateKeyException("Record already exists! See record " + recordNumber);
			}
		}
		final Integer recordNumber = getRecordNumber();
		cache.put(recordNumber, data);
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
	public synchronized void unlock(int recNo) throws RecordNotFoundException {
		if (isLockedRecord(recNo)) {
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
	public synchronized boolean isLocked(int recNo) throws RecordNotFoundException {
		if (isInvalidRecord(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid record");
		}
		return isLockedRecord(recNo);
	}

	private boolean isLockedRecord(int recNo) {
		return lockedRecords.contains(recNo);
	}

	private Integer getRecordNumber() {
		Integer deletedRecordNumber = null;
		for (Entry<Integer, String[]> record : cache.entrySet()) {
			if (record.getValue() == null) {
				deletedRecordNumber = new Integer(record.getKey());
				break;
			}
		}
		return (deletedRecordNumber == null) ? cache.size() : deletedRecordNumber;
	}

	private String getUniqueKey(String[] data) {
		final String name = data[0];
		final String location = data[1];
		final String uniqueKey = name + UNDERSCORE + location;
		return uniqueKey;
	}

	private boolean isInvalidRecord(int recNo) {
		return !getAllValidRecords().containsKey(recNo);
	}

	private Map<Integer, String[]> getAllValidRecords() {
		final Map<Integer, String[]> validRecords = new HashMap<>();
		for (Entry<Integer, String[]> record : cache.entrySet()) {
			final int recordNumber = record.getKey();
			final String[] fieldValues = record.getValue();
			if (fieldValues != null) {
				validRecords.put(new Integer(recordNumber), fieldValues);
			}
		}
		return validRecords;
	}

	// TODO: TEST METHODS BELOW, DELETE BEFORE SUBMISSION

	public int getTotalNumberOfRecords() {
		return cache.size();
	}

	public synchronized void clear() {
		cache.clear();
	}

	public synchronized void save() throws DatabaseException {
		dbAccessManager.persist(cache);
	}

	public synchronized void load() throws DatabaseException {
		dbAccessManager.read(cache);
	}
}
