package suncertify.db;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A factory for creating database manager objects.
 */
public final class DBFactory {

	/** The databaseManager object. */
	private static DBMainExtended databaseManager = Data.getInstance();

	private DBFactory(){
	}
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
	public static DBMainExtended getDatabase(final String dbFileLocation) throws DatabaseException {
		if (!Files.exists(Paths.get(dbFileLocation))) {
			throw new DatabaseException("Could not find the specified file: " + dbFileLocation);
		}
		databaseManager.initialize(dbFileLocation);
		return databaseManager;
	}
}
