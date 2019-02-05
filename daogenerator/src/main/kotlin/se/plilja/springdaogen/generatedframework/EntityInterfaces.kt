package se.plilja.springdaogen.generatedframework


fun entityInterfaces(_package: String): List<Pair<String, String>> {
    return listOf(
        createdAtTracked(_package),
        changedAtTracked(_package),
        versionTracked(_package)
    )
}

fun createdAtTracked(_package: String): Pair<String, String> {
    return Pair(
        "CreatedAtTracked", """
package $_package;

public interface CreatedAtTracked<T> {
    void setCreatedNow();

    T getCreatedAt();
}
    """.trimIndent()
    )
}

fun changedAtTracked(_package: String): Pair<String, String> {
    return Pair(
        "ChangedAtTracked", """
package $_package;

public interface ChangedAtTracked<T> {
    void setChangedNow();

    T getChangedAt();
}
    """.trimIndent()
    )
}

fun versionTracked(_package: String): Pair<String, String> {
    return Pair(
        "VersionTracked", """
package $_package;

public interface VersionTracked {

    void setVersion(Integer value);

    Integer getVersion();
}
    """.trimIndent()
    )
}
