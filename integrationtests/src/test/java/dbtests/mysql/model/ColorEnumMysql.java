package dbtests.mysql.model;

import dbtests.framework.BaseDatabaseEnum;

public enum ColorEnumMysql implements BaseDatabaseEnum<Integer> {

    RED(1),
    GREEN(2),
    BLUE(3);

    private final Integer id;

    ColorEnumMysql(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public static ColorEnumMysql fromId(Integer id) {
        if (id != null) {
            for (ColorEnumMysql value : values()) {
                if (value.getId().equals(id)) {
                    return value;
                }
            }
        }
        return null;
    }

}
