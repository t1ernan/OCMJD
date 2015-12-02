package suncertify.db;

import java.util.HashMap;
import java.util.Map;

import suncertify.domain.Contractor;

public class Data implements DBMainExtended {

	private final Map<Long, Contractor> cache = new HashMap<>();

	private String databasePath;

	public Data(String databasePath) {
		super();
		this.databasePath = databasePath;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(int recNo, String[] data) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] find(String[] criteria) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int create(String[] data) throws DuplicateKeyException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void lock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unlock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

}
