package dbtests.postgres.model;

public enum ColorEnumPostgres {

    BLUE("blue", "#0000FF"),
    GREEN("green", "#00FF00"),
    RED("red", "#FF0000");

    private final String id;
    private final String hex;

    ColorEnumPostgres(String id, String hex) {
        this.id = id;
        this.hex = hex;
    }

    public String getId() {
        return id;
    }

    public String getHex() {
        return hex;
    }

    public static ColorEnumPostgres fromId(String id) {
        if (id != null) {
            for (ColorEnumPostgres value : values()) {
                if (value.getId().equals(id)) {
                    return value;
                }
            }
        }
        return null;
    }

}
