package se.plilja.springdaogen.dbtests.oracle;

import se.plilja.springdaogen.dbtests.framework.BaseEntity;

public class BazOracleEntity implements BaseEntity<BazOracleEntity, Integer> {

    private Integer id;
    private String name;

    public BazOracleEntity() {
    }

    public BazOracleEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BazOracleEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public BazOracleEntity setId(Integer id) {
        this.id = id;
        return this;
    }

}
