package suncertify.db;

import java.util.Map;

/**
 * The class DBAccessManager is the common interface for all classes that
 * perform read/writes to a database.
 */
public interface DBAccessManager {

	/**
	 * Reads the the database and loads its records into the specified map.
	 *
	 * @param map
	 *            a map containing record numbers as the keys and their
	 *            corresponding records as the values
	 * @throws DatabaseException
	 *             if an unexpected IO exception occurs when trying to read the
	 *             file
	 */
	public void readDatabaseIntoCache(final Map<Integer, String[]> map) throws DatabaseException;

	/**
	 * Persists the the elements of the specified map to the database.
	 *
	 * @param map
	 *            a map containing record numbers as the keys and their
	 *            corresponding records as the values
	 * @throws DatabaseException
	 *             if an unexpected IO exception occurs when trying to write the
	 *             data to the database file
	 */
	public void persist(final Map<Integer, String[]> map) throws DatabaseException;

	/**
	 * Compares the size of the each element in the specified
	 * {@code fieldValues} against the max permitted field size described in the
	 * database schema.
	 *
	 * @param fieldValues
	 *            a string array where each element is a record value.
	 * @throws IllegalArgumentException
	 *             if the number of characters used in a field exceeds the max
	 *             number of characters permitted for that field
	 */
	public void validateFieldsAgainstSchema(final String[] fieldValues);

	/**
	 * The method is responsible setting the location of the database file with
	 * the specified {@code dbFileLocation} and initializing the other
	 * appropriate field variables using the schema information specified in the
	 * database file. Checks the magic cookie value and the record offset value
	 * specified in the file to ensure the correct database file with the
	 * correct schema information is being used.
	 *
	 * @param dbFileLocation
	 *            the location of the database on the file system.
	 * @throws DatabaseException
	 *             if the database file cannot be read.
	 */
	public void initialize(final String dbFileLocation) throws DatabaseException;
}
