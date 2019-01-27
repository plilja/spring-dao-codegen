package dbtests.oracle;

import dbtests.framework.BaseEntity;

public class OneColumnNaturalIdOracleEntity implements BaseEntity<String> {

    private String id;

    public OneColumnNaturalIdOracleEntity() {
    }

    public OneColumnNaturalIdOracleEntity(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
