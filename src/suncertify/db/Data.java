package suncertify.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import suncertify.util.ListConverter;

public class Data implements DBMainExtended {

	private final Map<Integer, String[]> cache = new HashMap<>();
	private final DBAccessManager dbAccessManager;
	private final Set<Integer> lockedRecords = new HashSet<>();

	public Data(String databasePath) throws DatabaseException {
		this.dbAccessManager = new DBAccessManager(databasePath);
		load();

		// Runtime.getRuntime().addShutdownHook(new Thread() {
		// @Override
		// public void run() {
		// try {
		// save();
		// } catch (DatabaseException e) {
		// System.err.println("Could not save data: " + e.getMessage());
		// }
		// }
		// });
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized String[] read(int recNo) throws RecordNotFoundException {
		validateRecord(recNo);
		return cache.get(recNo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void update(int recNo, String[] data) throws RecordNotFoundException {
		validateRecord(recNo);
		cache.put(recNo, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void delete(int recNo) throws RecordNotFoundException {
		validateRecord(recNo);
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
	public void lock(int recNo) throws RecordNotFoundException {
		while (isLocked(recNo)) {
			try {
				validateRecord(recNo);
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
	public void unlock(int recNo) throws RecordNotFoundException {
		lockedRecords.remove(recNo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		return lockedRecords.contains(recNo);
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

	public Integer getRecordNumber() {
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
		final String uniqueKey = name + "_" + location;
		return uniqueKey;
	}

	private void validateRecord(int recNo) throws RecordNotFoundException {
		if (!getAllValidRecords().containsKey(recNo)) {
			throw new RecordNotFoundException("Record " + recNo + " is not a valid record");
		}
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

	public int getTotalNumberOfRecords() {
		return cache.size();
	}
}
