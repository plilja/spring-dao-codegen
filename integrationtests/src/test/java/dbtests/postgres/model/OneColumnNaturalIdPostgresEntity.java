package dbtests.postgres.model;

import dbtests.framework.BaseEntity;

public class OneColumnNaturalIdPostgresEntity implements BaseEntity<String> {

    private String id;

    public OneColumnNaturalIdPostgresEntity() {
    }

    public OneColumnNaturalIdPostgresEntity(String id) {
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
