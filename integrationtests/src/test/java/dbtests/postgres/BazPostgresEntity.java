package dbtests.postgres;

import dbtests.framework.BaseEntity;

public class BazPostgresEntity implements BaseEntity<Integer> {

    private Integer bazId;
    private String bazName;

    public BazPostgresEntity() {
    }

    public BazPostgresEntity(Integer bazId, String bazName) {
        this.bazId = bazId;
        this.bazName = bazName;
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

    public Integer getId() {
        return bazId;
    }

    public void setId(Integer id) {
        this.bazId = id;
    }

}
