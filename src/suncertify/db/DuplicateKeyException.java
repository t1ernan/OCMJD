package suncertify.db;

/**
 * The class DuplicateKeyException is a direct subclass of
 * {@link DatabaseException}. It's thrown when attempting to add a new entry to
 * the database when the database already contains an existing entry with the
 * same primary key.
 */
public class DuplicateKeyException extends DatabaseException {

	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = 17011991;

	/**
	 * Constructs a new DuplicateKeyException with {@code null} as its detail
	 * message
	 */
	public DuplicateKeyException() {
		super();
	}

	/**
	 * Constructs a new DuplicateKeyException with the specified detail message
	 *
	 * @param message
	 *            the detail message
	 */
	public DuplicateKeyException(String message) {
		super(message);
	}
}
