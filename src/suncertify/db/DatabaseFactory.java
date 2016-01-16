/*
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.db;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A factory for retrieving and initializing a single instance of a data access object for the
 * specified database file. Marked final to prevent overriding.
 */
public final class DatabaseFactory {

  /** The databaseManager object. */
  private static DBMainExtended dao = Data.getInstance();

  /**
   * Private constructor to prevent instantiation by other classes.
   */
  private DatabaseFactory() {
  }

  /**
   * Gets a data access object for the database file with specified file path and initializes it.
   *
   * @param dbFilePath
   *          the file path of the database file.
   * @return the single instance of the data access object.
   * @throws DatabaseException
   *           if a databaseManager instance could not be created or if the specified file does not
   *           exist.
   */
  public static DBMainExtended getDatabase(final String dbFilePath) throws DatabaseException {
    if (!Files.exists(Paths.get(dbFilePath))) {
      throw new DatabaseException("Could not find the specified file: " + dbFilePath);
    }
    dao.initialize(dbFilePath);
    return dao;
  }
}
