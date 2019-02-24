package db;

/**
 * An attempt to update or delete a row by id was made,
 * but more than the expected number of rows was updated.
 * <p>
 * This probably indicates a faulty query.
 */
public class TooManyRowsUpdatedException extends DatabaseException {
    public TooManyRowsUpdatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyRowsUpdatedException(String message) {
        super(message);
    }
}