package suncertify.business;

/**
 * The class ServiceException is a direct subclass of {@link Exception} and a common superclass of a
 * number of business logic related exceptions.
 */
public class ServiceException extends Exception {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /**
   * Constructs a new ServiceException with {@code null} as its detail message.
   */
  public ServiceException() {
    super();
  }

  /**
   * Constructs a new ServiceException with the specified detail message.
   *
   * @param message
   *          the detail message
   */
  public ServiceException(final String message) {
    super(message);
  }

  /**
   * Constructs a new ServiceException with the specified detail message and cause.
   *
   * <p>
   * Note that the detail message associated with {@code cause} is <i>not</i> automatically
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
  public ServiceException(final String message, final Throwable throwable) {
    super(message, throwable);
  }

}
