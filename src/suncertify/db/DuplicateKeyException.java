/*
 * DuplicateKeyException.java  1.0  13-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.db;

/**
 * The class DuplicateKeyException is a direct subclass of {@link DatabaseException}. It's thrown
 * when attempting to add a new entry to the database if the database already contains an
 * entry, which hasn't been marked as deleted, with the same primary key.
 */
public class DuplicateKeyException extends DatabaseException {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /**
   * Constructs a new DuplicateKeyException with {@code null} as its detail message.
   */
  public DuplicateKeyException() {
    super();
  }

  /**
   * Constructs a new DuplicateKeyException with the specified detail message.
   *
   * @param message
   *          the detail message.
   */
  public DuplicateKeyException(final String message) {
    super(message);
  }
}
