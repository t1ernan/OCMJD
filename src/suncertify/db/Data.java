package suncertify.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Data implements DBMainExtended {

	private static final Map<Integer, String[]> cache = new HashMap<>();

	private final DBManager dbManager;

	public Data(String databasePath) throws DatabaseException {
		super();
		this.dbManager = new DBManager(databasePath);
		readData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized String[] read(int recNo) throws RecordNotFoundException {
		if (cache.containsKey(recNo)) {
			return cache.get(recNo);
		} else {
			throw new RecordNotFoundException("Could not find record: " + recNo);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void update(int recNo, String[] data) throws RecordNotFoundException {
		if (cache.containsKey(recNo)) {
			cache.put(recNo, data);
		} else {
			throw new RecordNotFoundException("Could not find record: " + recNo);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void delete(int recNo) throws RecordNotFoundException {
		if (cache.containsKey(recNo)) {
			cache.put(recNo, null);
		} else {
			throw new RecordNotFoundException("Could not find record: " + recNo);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized int[] find(String[] criteria) throws RecordNotFoundException {
		final List<Integer> matchingRecords = new ArrayList<>();
		for (Entry<Integer, String[]> record : cache.entrySet()) {
			Integer key = record.getKey();
			String[] fieldValues = record.getValue();
			for (int index = 0; index < fieldValues.length; index++) {

			}

		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized int create(String[] data) throws DuplicateKeyException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void lock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void unlock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean isLocked(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void saveData() throws DatabaseException {
		dbManager.saveData((cache));
	}

	private synchronized void readData() throws DatabaseException {
		dbManager.populateCache(cache);
	}

	public void printCache() {
		cache.forEach((k, v) -> System.out.println("Key: " + k + " , Values: \t name:" + v[0] + ", location: " + v[1]
				+ ", specialities:" + v[2] + ", size: " + v[3] + ", rate: " + v[4] + ", owner: " + v[5]));
	}
}
