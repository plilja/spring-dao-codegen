package db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OneColumnNaturalIdOracleH2 implements BaseEntity<String> {

    @Size(max = 9)
    private String id;

    public OneColumnNaturalIdOracleH2() {
    }

    public OneColumnNaturalIdOracleH2(String id) {
        this.id = id;
    }

    @JsonIgnore
    @Override
    public String getId() {
        return id;
    }

    @JsonIgnore
    @Override
    public void setId(String id) {
        this.id = id;
    }

}
