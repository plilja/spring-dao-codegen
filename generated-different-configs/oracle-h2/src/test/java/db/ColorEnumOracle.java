package db;

public enum ColorEnumOracle implements BaseDatabaseEnum<String> {

    BLUE("BLUE"),
    GREEN("GREEN"),
    RED("RED");

    private final String id;

    ColorEnumOracle(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public static ColorEnumOracle fromId(String id) {
        if (id != null) {
            for (ColorEnumOracle value : values()) {
                if (value.getId().equals(id)) {
                    return value;
                }
            }
        }
        return null;
    }

}
