package suncertify.db;

// TODO: Auto-generated Javadoc
/**
 * The Class Data.
 */
public class Data {

	/**
	 * Read.
	 *
	 * @param recNo the rec no
	 * @return the string[]
	 * @throws RecordNotFoundException the record not found exception
	 */
	public String[] read(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * Update.
	 *
	 * @param recNo the rec no
	 * @param data the data
	 * @throws RecordNotFoundException the record not found exception
	 */
	public void update(int recNo, String[] data) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * Delete.
	 *
	 * @param recNo the rec no
	 * @throws RecordNotFoundException the record not found exception
	 */
	public void delete(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * Find.
	 *
	 * @param criteria the criteria
	 * @return the int[]
	 * @throws RecordNotFoundException the record not found exception
	 */
	public int[] find(String[] criteria) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * Creates the.
	 *
	 * @param data the data
	 * @return the int
	 * @throws DuplicateKeyException the duplicate key exception
	 */
	public int create(String[] data) throws DuplicateKeyException {
		// TODO Auto-generated method stub
		return 0;
	}

	
	/**
	 * Lock.
	 *
	 * @param recNo the rec no
	 * @throws RecordNotFoundException the record not found exception
	 */
	public void lock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * Unlock.
	 *
	 * @param recNo the rec no
	 * @throws RecordNotFoundException the record not found exception
	 */
	public void unlock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * Checks if is locked.
	 *
	 * @param recNo the rec no
	 * @return true, if is locked
	 * @throws RecordNotFoundException the record not found exception
	 */
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

}
