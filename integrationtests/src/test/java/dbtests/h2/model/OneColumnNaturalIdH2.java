package dbtests.h2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dbtests.framework.BaseEntity;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OneColumnNaturalIdH2 implements BaseEntity<String> {

    @Size(max = 10)
    private String id;

    public OneColumnNaturalIdH2() {
    }

    public OneColumnNaturalIdH2(String id) {
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
