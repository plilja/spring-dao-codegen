package db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OneColumnNaturalIdMsSqlH2 implements BaseEntity<String> {

    @Size(max = 10)
    private String id;

    public OneColumnNaturalIdMsSqlH2() {
    }

    public OneColumnNaturalIdMsSqlH2(String id) {
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
