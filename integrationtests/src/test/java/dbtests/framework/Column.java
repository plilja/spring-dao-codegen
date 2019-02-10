package dbtests.framework;

public class Column<EntityType extends BaseEntity<?>, ValueType> {
    private final String name;

    public Column(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}