package dbtests.mysql.model;

import dbtests.framework.BaseEntity;

public class MBazMysql implements BaseEntity<Integer> {

    private Integer id;
    private String name;

    public MBazMysql() {
    }

    public MBazMysql(Integer id, String name) {
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
