package dbtests.mysql.model;

public class MBazViewMysql {

    private Integer colorEnumMysqlId;
    private Integer id;
    private String name;

    public MBazViewMysql() {
    }

    public MBazViewMysql(Integer colorEnumMysqlId, Integer id, String name) {
        this.colorEnumMysqlId = colorEnumMysqlId;
        this.id = id;
        this.name = name;
    }

    public Integer getColorEnumMysqlId() {
        return colorEnumMysqlId;
    }

    public void setColorEnumMysqlId(Integer colorEnumMysqlId) {
        this.colorEnumMysqlId = colorEnumMysqlId;
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
