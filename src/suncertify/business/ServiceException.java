package suncertify.business;

public class ServiceException extends Exception {

	/**
	 * Instantiates a new services exception.
	 */
	public ServiceException() {
		super();
	}

	/**
	 * Instantiates a new services exception.
	 *
	 * @param message
	 *            the message
	 */
	public ServiceException(String message) {
		super(message);
	}

}
