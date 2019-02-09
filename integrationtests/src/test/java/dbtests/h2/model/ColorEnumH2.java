package dbtests.h2.model;

public enum ColorEnumH2 {

    BLUE("blue", "#0000FF"),
    GREEN("green", "#00FF00"),
    RED("red", "#FF0000");

    private final String id;
    private final String hex;

    ColorEnumH2(String id, String hex) {
        this.id = id;
        this.hex = hex;
    }

    public String getId() {
        return id;
    }

    public String getHex() {
        return hex;
    }

    public static ColorEnumH2 fromId(String id) {
        if (id != null) {
            for (ColorEnumH2 value : values()) {
                if (value.getId().equals(id)) {
                    return value;
                }
            }
        }
        return null;
    }

}
