package db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OneColumnGeneratedIdMsSqlH2 implements BaseEntity<Integer> {

    private Integer id;

    public OneColumnGeneratedIdMsSqlH2() {
    }

    public OneColumnGeneratedIdMsSqlH2(Integer id) {
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
