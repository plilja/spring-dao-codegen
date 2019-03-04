package dbtests.postgres.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BazViewPostgresEntity {

    private Integer bazId;
    private String color;
    private String nameWithSpace;

    public BazViewPostgresEntity() {
    }

    public BazViewPostgresEntity(Integer bazId, String color, String nameWithSpace) {
        this.bazId = bazId;
        this.color = color;
        this.nameWithSpace = nameWithSpace;
    }

    public Integer getBazId() {
        return bazId;
    }

    public void setBazId(Integer bazId) {
        this.bazId = bazId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNameWithSpace() {
        return nameWithSpace;
    }

    public void setNameWithSpace(String nameWithSpace) {
        this.nameWithSpace = nameWithSpace;
    }

}
