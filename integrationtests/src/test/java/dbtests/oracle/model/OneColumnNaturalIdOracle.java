package dbtests.oracle.model;

import dbtests.framework.BaseEntity;
import javax.validation.constraints.Size;

public class OneColumnNaturalIdOracle implements BaseEntity<String> {

    @Size(max = 9)
    private String id;

    public OneColumnNaturalIdOracle() {
    }

    public OneColumnNaturalIdOracle(String id) {
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

    @Override
    public String toString() {
        return "OneColumnNaturalIdOracle{id=" + id + "}";
    }

}
