package suncertify.db;

/**
 * The class RecordNotFoundException is a direct subclass of {@link DatabaseException}. It's thrown
 * when querying the database for a record which does not exist or has been marked as deleted in the
 * database.
 */
public class RecordNotFoundException extends DatabaseException {

  /** The serial version UID. */
  private static final long serialVersionUID = 17011991;

  /**
   * Constructs a new RecordNotFoundException with {@code null} as its detail message.
   */
  public RecordNotFoundException() {
    super();
  }

  /**
   * Constructs a new RecordNotFoundException with the specified detail message.
   *
   * @param message
   *          the detail message
   */
  public RecordNotFoundException(final String message) {
    super(message);
  }
}
