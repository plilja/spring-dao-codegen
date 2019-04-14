package se.plilja.springdaogen.copyable


fun frameworkExceptions(_package: String): List<Pair<String, String>> {
    return listOf(
            databaseException(_package),
            tooManyRowsAvailableException(_package),
            tooManyRowsUpdatedException(_package),
            noRowsUpdatedException(_package)
    )
}

fun databaseException(_package: String): Pair<String, String> {
    return Pair(
            "DatabaseException", """
package $_package;

/**
 * Base class for data base exceptions.
 */
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(String message) {
        super(message);
    }
}
    """.trimIndent()
    )
}

fun tooManyRowsAvailableException(_package: String): Pair<String, String> {
    return Pair(
            "TooManyRowsAvailableException", """
package $_package;

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
    """.trimIndent()
    )
}

fun tooManyRowsUpdatedException(_package: String): Pair<String, String> {
    return Pair(
            "TooManyRowsUpdatedException", """
package $_package;

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
    """.trimIndent()
    )
}

fun noRowsUpdatedException(_package: String): Pair<String, String> {
    return Pair(
            "NoRowsUpdatedException", """
package $_package;

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

    """.trimIndent()
    )
}
