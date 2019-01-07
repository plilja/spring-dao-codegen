package se.plilja.springdaogen.generatedframework


fun baseEntity(_package: String): Pair<String, String> {
    return Pair("BaseEntity", """
package $_package;

public interface BaseEntity<ID> {
    void setId(ID id);

    ID getId();

}
""".trimIndent())

}