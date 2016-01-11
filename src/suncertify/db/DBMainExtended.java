package suncertify.db;

import java.io.IOException;

public interface DBMainExtended extends DBMain {

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
	int create(String[] data) throws DuplicateKeyException;

	/**
	 * Deletes a record, making the record number and associated disk storage
	 * available for reuse.
	 *
	 * @param recNo
	 *            the record number
	 */
	@Override
	void delete(int recNo);

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
	int[] find(String[] criteria) throws RecordNotFoundException;

	/**
	 * This method is responsible for initializing field variables, loading the
	 * contents of the database file specified into the in-memory cache and
	 * adding the shutdown hook to ensure that the contents of the cache is
	 * written back to the database file when the application is shut down.
	 */
	void initialize(String dbFileLocation) throws DatabaseException;

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
	boolean isLocked(int recNo) throws RecordNotFoundException;

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
	void lock(int recNo) throws RecordNotFoundException;

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
	String[] read(int recNo) throws RecordNotFoundException;

	void save() throws IOException;

	/**
	 * Releases the lock on a record.
	 *
	 * @param recNo
	 *            the record number
	 */
	@Override
	void unlock(int recNo);

	/**
	 * Modifies the fields of a record. The new value for field n appears in
	 * data[n].
	 *
	 * @param recNo
	 *            the record number
	 * @param data
	 *            a string array where each element is a record value
	 */
	@Override
	void update(int recNo, String[] data);

}
