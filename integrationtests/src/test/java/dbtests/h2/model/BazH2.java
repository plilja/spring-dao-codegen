package dbtests.h2.model;

import dbtests.framework.BaseEntity;

public class BazH2 implements BaseEntity<Integer> {

    private Integer bazId;
    private String bazName;

    public BazH2() {
    }

    public BazH2(Integer bazId, String bazName) {
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

    @Override
    public Integer getId() {
        return bazId;
    }

    @Override
    public void setId(Integer id) {
        this.bazId = id;
    }

}
