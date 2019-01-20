package se.plilja.springdaogen.generatedframework


fun frameworkExceptions(_package: String): List<Pair<String, String>> {
    return listOf(maxAllowedCountExceededException(_package), sqlUpdateException(_package))
}

fun maxAllowedCountExceededException(_package: String): Pair<String, String> {
    return Pair(
        "MaxAllowedCountExceededException", """
package $_package;

public class MaxAllowedCountExceededException extends RuntimeException {
    public MaxAllowedCountExceededException(String message) {
        super(message);
    }
}
    """.trimIndent()
    )
}

fun sqlUpdateException(_package: String): Pair<String, String> {
    return Pair(
        "SqlUpdateException", """
package $_package;

public class SqlUpdateException extends RuntimeException {
    public SqlUpdateException(String message) {
        super(message);
    }
}
    """.trimIndent()
    )
}