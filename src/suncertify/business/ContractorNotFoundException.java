package suncertify.business;

/**
 * The class ContractorNotFoundException is a direct subclass of
 * {@link ServiceException}. It's thrown when querying the database for a
 * contractor which does not exist or has been marked as deleted in the
 * database.
 */
public class ContractorNotFoundException extends ServiceException {

	/** The serial version UID. */
	private static final long serialVersionUID = 17011991;

	/**
	 * Constructs a new ContractorNotFoundException with {@code null} as its
	 * detail message.
	 */
	public ContractorNotFoundException() {
		super();
	}

	public ContractorNotFoundException(final String message, final Throwable throwable) {
		super(message, throwable);
	}
}
