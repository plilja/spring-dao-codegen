package se.plilja.springdaogen.generatedframework


fun columnClass(_package: String): Pair<String, String> {
    return Pair(
        "Column", """
package $_package;

public class Column<EntityType extends BaseEntity<?>, ValueType> {
    private final String name;

    public Column(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
    """.trimIndent())
}
