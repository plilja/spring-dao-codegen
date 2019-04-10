package se.plilja.springdaogen.copyable


fun baseDatabaseEnum(_package: String): Pair<String, String> {
    return Pair("BaseDatabaseEnum", """
package $_package;

public interface BaseDatabaseEnum<ID> {

    ID getId();

}
""".trimIndent())

}
