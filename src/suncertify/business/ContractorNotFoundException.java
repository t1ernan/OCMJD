/*
 * ContractorNotFoundException.java  1.0  12-Jan-2016
 *
 * Candidate: Tiernan Scully
 * Oracle Testing ID: OC1539331
 * Registration ID 292125773
 *
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment - English (ENU)
 */

package suncertify.business;

/**
 * The class ContractorNotFoundException is a direct subclass of {@link ServiceException}. It's
 * thrown when querying the database for a contractor which does not exist or has been marked as
 * deleted.
 */
public class ContractorNotFoundException extends Exception {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /**
   * Constructs a new ContractorNotFoundException with {@code null} as its detail message.
   */
  public ContractorNotFoundException() {
    super();
  }

  /**
   * Constructs a new ContractorNotFoundException with the specified detail message.
   *
   * @param message
   *          the detail message.
   */
  public ContractorNotFoundException(final String message) {
    super(message);
  }

  /**
   * Constructs a new ContractorNotFoundException with the specified detail message and cause.
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
  public ContractorNotFoundException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
