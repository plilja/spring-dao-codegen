package dbtests.h2.model;

import dbtests.framework.BaseEntity;

public class OneColumnGeneratedIdH2 implements BaseEntity<Integer> {

    private Integer id;

    public OneColumnGeneratedIdH2() {
    }

    public OneColumnGeneratedIdH2(Integer id) {
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

}
