package dbtests.mysql.model;

import dbtests.framework.BaseEntity;

public class OneColumnGeneratedIdMysqlEntity implements BaseEntity<Integer> {

    private Integer id;

    public OneColumnGeneratedIdMysqlEntity() {
    }

    public OneColumnGeneratedIdMysqlEntity(Integer id) {
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
