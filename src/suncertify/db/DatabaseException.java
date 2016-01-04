package suncertify.db;

/**
 * The class DatabaseException is a direct subclass of {@link Exception} and a
 * common superclass of a number of database-related exceptions.
 */
public class DatabaseException extends Exception {

	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = 17011991;

	/**
	 * Constructs a new DatabaseException with {@code null} as its detail
	 * message
	 */
	public DatabaseException() {
		super();
	}

	/**
	 * Constructs a new DatabaseException with the specified detail message
	 *
	 * @param message
	 *            the detail message
	 */
	public DatabaseException(String message) {
		super(message);
	}
}
