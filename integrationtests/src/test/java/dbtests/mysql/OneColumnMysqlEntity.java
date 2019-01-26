package dbtests.mysql;

import dbtests.framework.BaseEntity;

public class OneColumnMysqlEntity implements BaseEntity<Integer> {

    private Integer id;

    public OneColumnMysqlEntity() {
    }

    public OneColumnMysqlEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
