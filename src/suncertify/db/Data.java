package suncertify.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Data implements DBMainExtended {

	private static final Map<Integer, String[]> cache = new HashMap<>();
	private static final List<Integer> lockedRecords = new ArrayList<>();

	private final DBAccessManager dbManager;

	public Data(String databasePath) throws DatabaseException {
		super();
		this.dbManager = new DBAccessManager(databasePath);
		load();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
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
	public int[] find(String[] criteria) throws RecordNotFoundException {
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
		final int[] matchingRecordNumbers = convertIntegerListToArray(recordNumberList);
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
				throw new DuplicateKeyException("Record unique key already exists! See record " + recordNumber);
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
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		validateRecordExists(recNo);
		final boolean isLocked = lockedRecords.contains(recNo);
		return isLocked;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save() throws DatabaseException {
		dbManager.persist(cache);
	}

	public void load() throws DatabaseException {
		cache.clear();
		dbManager.read(cache);
	}

	private int[] convertIntegerListToArray(List<Integer> list) {
		final int[] intArray = new int[list.size()];
		for (int index = 0; index < list.size(); index++) {
			intArray[index] = list.get(index);
		}
		return intArray;
	}

	private synchronized Integer getRecordNumber() {
		final List<Integer> reusableNumbers = getDeletedRecordNumbers();
		if (reusableNumbers.isEmpty()) {
			return cache.size();
		} else {
			return reusableNumbers.get(0);
		}
	}

	private String getUniqueKey(String[] data) {
		final String name = data[0];
		final String location = data[1];
		final String uniqueKey = name + "_" + location;
		return uniqueKey;
	}

	private void validateRecordExists(int recNo) throws RecordNotFoundException {
		if (getDeletedRecordNumbers().contains(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " has been deleted");
		} else if (!getAllValidRecords().containsKey(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid record");
		}
	}

	public List<Integer> getDeletedRecordNumbers() {
		List<Integer> deletedRecordNumbers = new ArrayList<>();
		for (Entry<Integer, String[]> record : cache.entrySet()) {
			if (record.getValue() == null) {
				deletedRecordNumbers.add(new Integer(record.getKey()));
			}
		}

		return deletedRecordNumbers;
	}

	public Map<Integer, String[]> getAllValidRecords() {
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

	public int getTotalRecords() {
		return cache.size();
	}

	public void printCache() {
		cache.forEach((k, v) -> System.out.println("Key: " + k + " , Values: \t name:" + v[0] + ", location: " + v[1]
				+ ", specialities:" + v[2] + ", size: " + v[3] + ", rate: " + v[4] + ", owner: " + v[5]));
	}
}
