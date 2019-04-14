package se.plilja.springdaogen.copyable


fun entityInterfaces(_package: String): List<Pair<String, String>> {
    return listOf(
            createdByTracked(_package),
            changedByTracked(_package),
            createdAtTracked(_package),
            changedAtTracked(_package),
            versionTracked(_package)
    )
}

fun createdByTracked(_package: String): Pair<String, String> {
    return Pair(
            "CreatedByTracked", """
package $_package;

/**
 * An entity with a column tracking who/what
 * it was created by.
 */
public interface CreatedByTracked {
    void setCreatedBy(String value);

    String getCreatedBy();
}
    """.trimIndent()
    )
}

fun changedByTracked(_package: String): Pair<String, String> {
    return Pair(
            "ChangedByTracked", """
package $_package;

/**
 * An entity with a column tracking who/what
 * it was last changed by.
 */
public interface ChangedByTracked {
    void setChangedBy(String value);

    String getChangedBy();
}
    """.trimIndent()
    )
}

fun createdAtTracked(_package: String): Pair<String, String> {
    return Pair(
            "CreatedAtTracked", """
package $_package;

/**
 * An entity with a column tracking when
 * it was last created.
 */
public interface CreatedAtTracked<T> {
    /**
     * Mark the entity as created right now.
     */
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

/**
 * An entity with a column tracking when
 * it was last changed.
 */
public interface ChangedAtTracked<T> {
    /**
     * Mark the entity as changed right now.
     */
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

/**
 * An entity with a column tracking the version
 * of the entity. This protects it from concurrent
 * updates with stale state.
 */
public interface VersionTracked {

    void setVersion(Integer value);

    Integer getVersion();
}
    """.trimIndent()
    )
}
