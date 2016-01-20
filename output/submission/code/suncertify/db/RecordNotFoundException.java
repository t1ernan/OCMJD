/*
 * RecordNotFoundException.java  1.0  13-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.db;

/**
 * The class RecordNotFoundException is thrown when trying to perform a database operation a record
 * which does not exist or has been marked as deleted in the database.
 */
public class RecordNotFoundException extends Exception {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /**
   * Constructs a new RecordNotFoundException with {@code null} as its detail message.
   */
  public RecordNotFoundException() {
    super();
  }

  /**
   * Constructs a new RecordNotFoundException with the specified detail message.
   *
   * @param message
   *          the detail message.
   */
  public RecordNotFoundException(final String message) {
    super(message);
  }
}
