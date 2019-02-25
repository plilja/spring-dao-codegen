package dbtests.oracle.model;

import dbtests.framework.BaseEntity;

public class OneColumnGeneratedIdOracle implements BaseEntity<Integer> {

    private Integer id;

    public OneColumnGeneratedIdOracle() {
    }

    public OneColumnGeneratedIdOracle(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "OneColumnGeneratedIdOracle{id=" + id + "}";
    }

}
