package suncertify.business;

/**
 * The class ServiceException is a direct subclass of {@link Exception} and a
 * common superclass of a number of business logic related exceptions.
 */
public class ServiceException extends Exception {

	/** The serial version UID. */
	private static final long serialVersionUID = 17011991;

	/**
	 * Constructs a new ServiceException with {@code null} as its detail
	 * message.
	 */
	public ServiceException() {
		super();
	}

	/**
	 * Constructs a new ServiceException with the specified detail message.
	 *
	 * @param message
	 *            the detail message
	 */
	public ServiceException(final String message) {
		super(message);
	}

	public ServiceException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

}
