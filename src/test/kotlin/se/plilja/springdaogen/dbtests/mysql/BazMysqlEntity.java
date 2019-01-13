package se.plilja.springdaogen.dbtests.mysql;

import se.plilja.springdaogen.dbtests.framework.BaseEntity;

public class BazMysqlEntity implements BaseEntity<BazMysqlEntity, Integer> {

    private Integer id;
    private String name;

    public BazMysqlEntity() {
    }

    public BazMysqlEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BazMysqlEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public BazMysqlEntity setId(Integer id) {
        this.id = id;
        return this;
    }

}
