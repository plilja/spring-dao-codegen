package se.plilja.springdaogen.dbtests.mssql;

import se.plilja.springdaogen.dbtests.framework.BaseEntity;

public class BazMsSqlEntity implements BaseEntity<BazMsSqlEntity, Integer> {

    private Integer id;
    private String name;

    public BazMsSqlEntity() {
    }

    public BazMsSqlEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BazMsSqlEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public BazMsSqlEntity setId(Integer id) {
        this.id = id;
        return this;
    }

}
