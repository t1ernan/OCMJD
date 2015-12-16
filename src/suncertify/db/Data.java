package suncertify.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import suncertify.util.ListConverter;

public class Data implements DBMainExtended {

	private static final Map<Integer, String[]> cache = new HashMap<>();
	private static final List<Integer> lockedRecords = new ArrayList<>();

	private final DBAccessManager dbAccessManager;

	public Data(String databasePath) throws DatabaseException {
		super();
		this.dbAccessManager = new DBAccessManager(databasePath);
		load();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized String[] read(int recNo) throws RecordNotFoundException {
		validateRecordExists(recNo);
		return cache.get(recNo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void update(int recNo, String[] data) throws RecordNotFoundException {
		validateRecordExists(recNo);
		cache.put(recNo, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void delete(int recNo) throws RecordNotFoundException {
		validateRecordExists(recNo);
		cache.put(recNo, null);
	}

	/**
	 * {@inheritDoc}
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
	 * {@inheritDoc}
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
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void lock(int recNo) throws RecordNotFoundException {
		while (isLocked(recNo)) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		lockedRecords.add(new Integer(recNo));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void unlock(int recNo) throws RecordNotFoundException {
		lockedRecords.remove(new Integer(recNo));
		notify();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean isLocked(int recNo) throws RecordNotFoundException {
		validateRecordExists(recNo);
		final boolean isLocked = lockedRecords.contains(recNo);
		return isLocked;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save() throws DatabaseException {
		dbAccessManager.persist(cache);
	}

	public void load() throws DatabaseException {
		dbAccessManager.read(cache);
	}

	public void clear() {
		cache.clear();
	}

	private synchronized Integer getRecordNumber() {
		final Integer recordNumber = getReusableRecordNumber();
		return (recordNumber == null) ? cache.size() : recordNumber;

	}

	private synchronized String getUniqueKey(String[] data) {
		final String name = data[0];
		final String location = data[1];
		final String uniqueKey = name + "_" + location;
		return uniqueKey;
	}

	private synchronized void validateRecordExists(int recNo) throws RecordNotFoundException {
		if (!getAllValidRecords().containsKey(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid record");
		}
	}

	public synchronized Integer getReusableRecordNumber() {
		Integer deletedRecordNumber = null;
		for (Entry<Integer, String[]> record : cache.entrySet()) {
			if (record.getValue() == null) {
				deletedRecordNumber = new Integer(record.getKey());
				break;
			}
		}

		return deletedRecordNumber;
	}

	public synchronized Map<Integer, String[]> getAllValidRecords() {
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

	public void printCache() {
		cache.forEach((k, v) -> System.out.println("Key: " + k + " , Values: \t name:" + v[0] + ", location: " + v[1]
				+ ", specialities:" + v[2] + ", size: " + v[3] + ", rate: " + v[4] + ", owner: " + v[5]));
	}

	public int getTotalNumberOfRecords() {
		return cache.size();
	}
}
