package suncertify.ui;

public class LaunchException extends Exception{

	/** The serial version UID. */
	private static final long serialVersionUID = 17011991;

	/**
	 * Constructs a new LaunchException with {@code null} as its detail
	 * message.
	 */
	public LaunchException() {
		super();
	}

	/**
	 * Constructs a new LaunchException with the specified detail
	 * message.
	 *
	 * @param message
	 *            the detail message
	 */
	public LaunchException(final String message) {
		super(message);
	}
}
