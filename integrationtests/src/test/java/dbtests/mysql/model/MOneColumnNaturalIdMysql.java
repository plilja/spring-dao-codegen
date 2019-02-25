package dbtests.mysql.model;

import dbtests.framework.BaseEntity;

public class MOneColumnNaturalIdMysql implements BaseEntity<String> {

    private String id;

    public MOneColumnNaturalIdMysql() {
    }

    public MOneColumnNaturalIdMysql(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MOneColumnNaturalIdMysql{id=" + id + "}";
    }

}
