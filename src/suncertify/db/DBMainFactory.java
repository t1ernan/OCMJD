package suncertify.db;

/**
 * A factory for creating DBMain objects.
 */
public class DBMainFactory {

	/** The database object. */
	private static DBMain database;

	/**
	 * Gets a database instance. This method should only be called once upon
	 * startup to ensure there is a single instance of the database.
	 *
	 * @param dbFileLocation
	 *            the location of the database on the file system
	 * @return the database
	 * @throws DatabaseException
	 *             if a database instance could not be created
	 */
	public static DBMain getDatabase(final String dbFileLocation) throws DatabaseException {
		if (database == null) {
			database = new Data(dbFileLocation);
		}
		return database;
	}
}
