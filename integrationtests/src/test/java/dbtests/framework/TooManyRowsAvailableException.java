package dbtests.framework;

/**
 * Thrown to indicate that there were more rows than expected
 * available when performing a query. This might be ok depending
 * on the use case but if too many rows were returned
 * unexpectedly it might cause performance issues or
 * memory overflow.
 * <p>
 * If you know that you expect many rows you should signal
 * that to when performing your query.
 */
public class TooManyRowsAvailableException extends DatabaseException {
    public TooManyRowsAvailableException(String message) {
        super(message);
    }

    public TooManyRowsAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}