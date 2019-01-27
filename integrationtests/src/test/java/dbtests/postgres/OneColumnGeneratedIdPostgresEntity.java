package dbtests.postgres;

import dbtests.framework.BaseEntity;

public class OneColumnGeneratedIdPostgresEntity implements BaseEntity<Integer> {

    private Integer id;

    public OneColumnGeneratedIdPostgresEntity() {
    }

    public OneColumnGeneratedIdPostgresEntity(Integer id) {
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
