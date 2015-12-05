package suncertify.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Data implements DBMainExtended {

	private static final Map<Integer, String[]> cache = new HashMap<>();

	private final DBAccessManager dbManager;

	public Data(String databasePath) throws DatabaseException {
		super();
		this.dbManager = new DBAccessManager(databasePath);
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
		final List<Integer> recordNumberList = new ArrayList<>();
		for (Entry<Integer, String[]> record : cache.entrySet()) {
			boolean isMatch = true;
			final Integer recordNumber = record.getKey();
			final String[] fieldValues = record.getValue();
			for (int index = 0; index < criteria.length; index++) {
				final String recordValue = fieldValues[index].toUpperCase();
				final String searchValue = criteria[index].toUpperCase();
				if(!recordValue.startsWith(searchValue)){
					isMatch =false;
					break;
				}
			}
			if(isMatch){
				recordNumberList.add(recordNumber);
			}
		}
		if(recordNumberList.isEmpty()){
			throw new RecordNotFoundException("No matching records for selected criteria");
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
		for (Entry<Integer, String[]> record : cache.entrySet()) {
			final Integer recordNumber = record.getKey();
			final String[] fieldValues = record.getValue();
			final String existingUniqueKey = getUniqueKey(fieldValues);
			if(newUniqueKey.equalsIgnoreCase(existingUniqueKey)){
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


	private int[] convertIntegerListToArray(List<Integer> list) {
		final int[] intArray = new int[list.size()];
		for( int index=0; index < list.size(); index++ ) {
			intArray[index] = list.get(index);
		}
		return intArray;
	}
	
	private Integer getRecordNumber() {	
		for (Entry<Integer, String[]> record : cache.entrySet()) {
			final Integer recordNumber = record.getKey();
			final String[] fieldValues = record.getValue();
			if(fieldValues == null){
				return recordNumber;
			}
		}
		return cache.size();
	}

	private String getUniqueKey(String[] data) { 
		final String name = data[0];
		final String location = data[1];
		final String uniqueKey = name + "_" + location;
		return uniqueKey;
	}
	
	public void printCache() {
		cache.forEach((k, v) -> System.out.println("Key: " + k + " , Values: \t name:" + v[0] + ", location: " + v[1]
				+ ", specialities:" + v[2] + ", size: " + v[3] + ", rate: " + v[4] + ", owner: " + v[5]));
	}
}
