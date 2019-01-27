package dbtests.oracle.model;

import dbtests.framework.BaseEntity;

public class BazOracle implements BaseEntity<Integer> {

    private Integer id;
    private String name;

    public BazOracle() {
    }

    public BazOracle(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

}
