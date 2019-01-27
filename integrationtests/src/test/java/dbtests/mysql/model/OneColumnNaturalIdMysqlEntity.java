package dbtests.mysql.model;

import dbtests.framework.BaseEntity;

public class OneColumnNaturalIdMysqlEntity implements BaseEntity<String> {

    private String id;

    public OneColumnNaturalIdMysqlEntity() {
    }

    public OneColumnNaturalIdMysqlEntity(String id) {
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

}
