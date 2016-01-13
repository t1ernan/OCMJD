/*
 * DBMainExtended.java  1.0  13-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.db;

import java.io.IOException;

/**
 * DBMainExtended extends DBMain, the common interface for any data access objects in the
 * persistence layer. It defines two extra methods, {@code initialize} and {@code save}, used for
 * initializing the data access object on and persisting records to disk respectively.
 */
public interface DBMainExtended extends DBMain {

  /**
   * Creates a new record in the database (possibly reusing a deleted entry). Inserts the given
   * data, and returns the record number of the new record.
   *
   * @param data
   *          a string array where each element is a record value.
   * @return the record number of the new record.
   * @throws DuplicateKeyException
   *           If an existing record in the database, which has not been marked as deleted, contains
   *           the same key specified in the given data.
   */
  @Override
  int create(String[] data) throws DuplicateKeyException;

  /**
   * Deletes a record, making the record number and associated disk storage available for reuse.
   *
   * @param recNo
   *          the record number.
   */
  @Override
  void delete(int recNo);

  /**
   * Returns an array of record numbers that match the specified criteria. Field n in the database
   * file is described by criteria[n]. A null value in criteria[n] matches any field value. A
   * non-null value in criteria[n] matches any field value that begins with criteria[n]. (For
   * example, "Fred" matches "Fred" or "Freddy".)
   *
   * @param criteria
   *          the criteria.
   * @return an array of record numbers that match the specified criteria.
   * @throws RecordNotFoundException
   *           If the specified record does not exist or is marked as deleted in the database.
   */
  @Override
  int[] find(String[] criteria) throws RecordNotFoundException;

  /**
   * This method is responsible for initializing field variables, loading the contents of the
   * database file specified into the in-memory cache and adding the shutdown hook to ensure that
   * the contents of the cache is written back to the database file when the application is shut
   * down.
   *
   * @param dbFilePath
   *          the filePath of the database file.
   * @throws DatabaseException
   *           If the specified database file does not contain the expected magic cookie value or
   *           some I/O related exception occurred when attempting to read the file.
   */
  void initialize(String dbFilePath) throws DatabaseException;

  /**
   * Determines if a record is currently locked. Returns true if the record is locked, false
   * otherwise.
   *
   * @param recNo
   *          the record number
   * @return true, if is locked
   * @throws RecordNotFoundException
   *           If the specified record does not exist or is marked as deleted in the database
   */
  @Override
  boolean isLocked(int recNo) throws RecordNotFoundException;

  /**
   * Locks a record so that it can only be updated or deleted by this client. If the specified
   * record is already locked, the current thread gives up the CPU and consumes no CPU cycles until
   * the record is unlocked.
   *
   * @param recNo
   *          the record number
   * @throws RecordNotFoundException
   *           If the specified record does not exist or is marked as deleted in the database
   */
  @Override
  void lock(int recNo) throws RecordNotFoundException;

  /**
   * Reads a record from the file. Returns an array where each element is a record value
   *
   * @param recNo
   *          the record number
   * @return a string array where each element is a record value
   * @throws RecordNotFoundException
   *           If the specified record does not exist or is marked as deleted in the database
   */
  @Override
  String[] read(int recNo) throws RecordNotFoundException;

  /**
   * Writes the record cache to the database file, overwriting the existing record data stored in
   * the database file.
   *
   * @throws IOException
   *           Signals that an I/O exception has occurred when writing the records to disk.
   */
  void save() throws IOException;

  /**
   * Releases the lock on a record.
   *
   * @param recNo
   *          the record number
   */
  @Override
  void unlock(int recNo);

  /**
   * Modifies the fields of a record. The new value for field n appears in data[n].
   *
   * @param recNo
   *          the record number
   * @param data
   *          a string array where each element is a record value
   */
  @Override
  void update(int recNo, String[] data);

}
