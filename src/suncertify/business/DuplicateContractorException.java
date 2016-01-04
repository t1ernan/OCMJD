package suncertify.business;

public class DuplicateContractorException extends ServiceException {

	/**
	 * Instantiates a new DuplicateContractor exception.
	 */
	public DuplicateContractorException() {
		super();
	}

	/**
	 * Instantiates a new DuplicateContractor exception.
	 *
	 * @param message
	 *            the message
	 */
	public DuplicateContractorException(String message) {
		super(message);
	}
}
