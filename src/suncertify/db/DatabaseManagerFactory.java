package suncertify.db;

/**
 * A factory for creating database manager objects. It contains
 */
public class DatabaseManagerFactory {

	/** The databaseManager object. */
	private static DBMainExtended databaseManager;

	/**
	 * Gets a database manager for the database file at specified location. This
	 * method should ensure there is only one instance of the database manager
	 * for the application.
	 *
	 * @param dbFileLocation
	 *            the location of the databaseManager on the file system
	 * @return the databaseManager
	 * @throws DatabaseException
	 *             if a databaseManager instance could not be created
	 */
	public static DBMainExtended getDatabaseManager(final String dbFileLocation) throws DatabaseException {
		if (databaseManager == null) {
			databaseManager = Data.getInstance();
			databaseManager.initialize(dbFileLocation);
		}
		return databaseManager;
	}
}
