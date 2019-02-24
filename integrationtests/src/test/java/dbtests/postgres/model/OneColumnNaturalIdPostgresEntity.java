package dbtests.postgres.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dbtests.framework.BaseEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OneColumnNaturalIdPostgresEntity implements BaseEntity<String> {

    private String id;

    public OneColumnNaturalIdPostgresEntity() {
    }

    public OneColumnNaturalIdPostgresEntity(String id) {
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
