package dbtests.mssql.model;

public enum ColorEnumMsSql {

    BLUE("blue"),
    GREEN("green"),
    RED("red");

    private final String id;

    ColorEnumMsSql(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static ColorEnumMsSql fromId(String id) {
        if (id != null) {
            for (ColorEnumMsSql value : values()) {
                if (value.getId().equals(id)) {
                    return value;
                }
            }
        }
        return null;
    }

}
