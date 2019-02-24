package db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OneColumnGeneratedIdOracleH2 implements BaseEntity<Integer> {

    private Integer id;

    public OneColumnGeneratedIdOracleH2() {
    }

    public OneColumnGeneratedIdOracleH2(Integer id) {
        this.id = id;
    }

    @JsonIgnore
    @Override
    public Integer getId() {
        return id;
    }

    @JsonIgnore
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

}
