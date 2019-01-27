package dbtests.oracle;

import dbtests.framework.BaseEntity;

public class OneColumnGeneratedIdOracleEntity implements BaseEntity<Integer> {

    private Integer id;

    public OneColumnGeneratedIdOracleEntity() {
    }

    public OneColumnGeneratedIdOracleEntity(Integer id) {
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
