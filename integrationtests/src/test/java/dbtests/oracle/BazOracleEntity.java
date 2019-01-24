package dbtests.oracle;

import dbtests.framework.BaseEntity;

public class BazOracleEntity implements BaseEntity<Integer> {

    private Integer id;
    private String name;

    public BazOracleEntity() {
    }

    public BazOracleEntity(Integer id, String name) {
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
