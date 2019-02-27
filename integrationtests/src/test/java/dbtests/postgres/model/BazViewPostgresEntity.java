package dbtests.postgres.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BazViewPostgresEntity {

    private Integer bazId;
    private String bazName;
    private String color;

    public BazViewPostgresEntity() {
    }

    public BazViewPostgresEntity(Integer bazId, String bazName, String color) {
        this.bazId = bazId;
        this.bazName = bazName;
        this.color = color;
    }

    public Integer getBazId() {
        return bazId;
    }

    public void setBazId(Integer bazId) {
        this.bazId = bazId;
    }

    public String getBazName() {
        return bazName;
    }

    public void setBazName(String bazName) {
        this.bazName = bazName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
