package dbtests.oracle.model;

import dbtests.framework.BaseEntity;

public class OneColumnNaturalIdOracle implements BaseEntity<String> {

    private String id;

    public OneColumnNaturalIdOracle() {
    }

    public OneColumnNaturalIdOracle(String id) {
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
