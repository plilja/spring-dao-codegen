package dbtests.oracle.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BazViewOracle {

    @Size(max = 9)
    private String color;
    @NotNull
    private Integer id;
    @NotNull
    @Size(max = 30)
    private String name;

    public BazViewOracle() {
    }

    public BazViewOracle(String color, Integer id, String name) {
        this.color = color;
        this.id = id;
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
