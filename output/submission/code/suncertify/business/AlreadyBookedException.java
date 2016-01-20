/*
 * AlreadyBookedException.java  1.0  12-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.business;

/**
 * The class AlreadyBookedException is thrown when attempting to book a contractor which has been
 * marked as booked in the database, i.e. the contractor's customer ID field is not empty.
 */
public class AlreadyBookedException extends Exception {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /**
   * Constructs a new AlreadyBookedException with {@code null} as its detail message.
   */
  public AlreadyBookedException() {
    super();
  }

  /**
   * Constructs a new AlreadyBookedException with the specified detail message.
   *
   * @param message
   *          the detail message.
   */
  public AlreadyBookedException(final String message) {
    super(message);
  }
}
