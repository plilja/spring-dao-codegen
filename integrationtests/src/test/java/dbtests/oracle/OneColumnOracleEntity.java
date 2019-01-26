package dbtests.oracle;

import dbtests.framework.BaseEntity;

public class OneColumnOracleEntity implements BaseEntity<Integer> {

    private Integer id;

    public OneColumnOracleEntity() {
    }

    public OneColumnOracleEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}