package dbtests.mysql.model;

import dbtests.framework.BaseEntity;

public class MOneColumnGeneratedIdMysql implements BaseEntity<Integer> {

    private Integer id;

    public MOneColumnGeneratedIdMysql() {
    }

    public MOneColumnGeneratedIdMysql(Integer id) {
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
