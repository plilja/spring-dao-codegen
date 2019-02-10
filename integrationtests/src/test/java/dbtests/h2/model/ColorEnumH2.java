package dbtests.h2.model;

import dbtests.framework.BaseDatabaseEnum;

public enum ColorEnumH2 implements BaseDatabaseEnum<String> {

    BLUE("blue", "#0000FF"),
    GREEN("green", "#00FF00"),
    RED("red", "#FF0000");

    private final String id;
    private final String hex;

    ColorEnumH2(String id, String hex) {
        this.id = id;
        this.hex = hex;
    }

    public String getHex() {
        return hex;
    }

    @Override
    public String getId() {
        return id;
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
