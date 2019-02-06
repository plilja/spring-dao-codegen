package dbtests.framework;

/**
 * An attempt to update or delete a row by id was performed but
 * no row was affected. If you are using version columns
 * this probably indicates stale state.
 * <p>
 * It might also indicate a faulty query or an invalid id.
 */
public class NoRowsUpdatedException extends DatabaseException {
    public NoRowsUpdatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRowsUpdatedException(String message) {
        super(message);
    }
}
