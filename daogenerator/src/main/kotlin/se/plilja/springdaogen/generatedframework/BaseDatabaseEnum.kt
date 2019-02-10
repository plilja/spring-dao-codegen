package se.plilja.springdaogen.generatedframework


fun baseDatabaseEnum(_package: String): Pair<String, String> {
    return Pair("BaseDatabaseEnum", """
package $_package;

public interface BaseDatabaseEnum<ID> {

    ID getId();

}
""".trimIndent())

}
