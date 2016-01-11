package suncertify.business;

/**
 * The class AlreadyBookedException is a direct subclass of
 * {@link ServiceException}. It's thrown when attempting to book a Contractor
 * which has been marked as booked in the database, i.e. the Contractor already
 * has a valid CustomerId value.
 */
public class AlreadyBookedException extends ServiceException {

	/** The serial version UID. */
	private static final long serialVersionUID = 17011991;

	/**
	 * Constructs a new AlreadyBookedException with {@code null} as its detail
	 * message.
	 */
	public AlreadyBookedException() {
		super();
	}

	public AlreadyBookedException(final String message) {
		super(message);
	}
}
