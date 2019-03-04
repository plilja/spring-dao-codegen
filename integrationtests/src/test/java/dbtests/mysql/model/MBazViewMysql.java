package dbtests.mysql.model;

public class MBazViewMysql {

    private Integer colorEnumMysqlId;
    private Integer id;
    private String nameWithSpace;

    public MBazViewMysql() {
    }

    public MBazViewMysql(Integer colorEnumMysqlId, Integer id, String nameWithSpace) {
        this.colorEnumMysqlId = colorEnumMysqlId;
        this.id = id;
        this.nameWithSpace = nameWithSpace;
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

    public String getNameWithSpace() {
        return nameWithSpace;
    }

    public void setNameWithSpace(String nameWithSpace) {
        this.nameWithSpace = nameWithSpace;
    }

}
