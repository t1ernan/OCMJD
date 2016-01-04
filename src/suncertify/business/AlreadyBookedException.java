package suncertify.business;

public class AlreadyBookedException extends ServiceException {

	/**
	 * Instantiates a new ContractorNotFound exception.
	 */
	public AlreadyBookedException() {
		super();
	}

	/**
	 * Instantiates a new ContractorNotFound exception.
	 *
	 * @param message
	 *            the message
	 */
	public AlreadyBookedException(String message) {
		super(message);
	}
}
