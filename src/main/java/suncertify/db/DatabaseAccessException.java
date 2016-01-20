/*
 * DatabaseException.java  1.0  13-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.db;

/**
 * The class DatabaseAccessException is thrown when the database file that is selected does not
 * exist or the database file could not be read for some IO related reason.
 */
public class DatabaseAccessException extends Exception {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /**
   * Constructs a new InvalidDatabaseException with {@code null} as its detail message.
   */
  public DatabaseAccessException() {
    super();
  }

  /**
   * Constructs a new InvalidDatabaseException with the specified detail message.
   *
   * @param message
   *          the detail message
   */
  public DatabaseAccessException(final String message) {
    super(message);
  }

  /**
   * Constructs a new DatabaseAccessException with the specified detail message and cause.
   *
   * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
   * incorporated in this exception's detail message.
   *
   * @param message
   *          the detail message (which is saved for later retrieval by the {@link #getMessage()}
   *          method).
   * @param cause
   *          the cause (which is saved for later retrieval by the {@link #getCause()} method). (A
   *          <tt>null</tt> value is permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   */
  public DatabaseAccessException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
