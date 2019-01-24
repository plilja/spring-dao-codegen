package dbtests.mysql;

import dbtests.framework.BaseEntity;

public class BazMysqlEntity implements BaseEntity<Integer> {

    private Integer id;
    private String name;

    public BazMysqlEntity() {
    }

    public BazMysqlEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
