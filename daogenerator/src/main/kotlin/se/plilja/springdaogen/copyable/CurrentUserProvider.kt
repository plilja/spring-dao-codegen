package se.plilja.springdaogen.copyable


fun currentUserProvider(_package: String): Pair<String, String> {
    return Pair(
            "CurrentUserProvider", """
package $_package;

/**
 * Determines what should be written to
 * to a createdBy/changedBy-column.
 */
public interface CurrentUserProvider {
    String getCurrentUser();
}
    """.trimIndent()
    )
}
