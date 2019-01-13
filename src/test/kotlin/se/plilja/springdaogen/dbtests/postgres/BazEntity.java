package se.plilja.springdaogen.dbtests.postgres;

import se.plilja.springdaogen.dbtests.framework.BaseEntity;

public class BazEntity implements BaseEntity<Integer> {

    private Integer bazId;
    private String bazName;

    public BazEntity() {
    }

    public BazEntity(Integer bazId, String bazName) {
        this.bazId = bazId;
        this.bazName = bazName;
    }

    public Integer getBazId() {
        return bazId;
    }

    public BazEntity setBazId(Integer bazId) {
        this.bazId = bazId;
        return this;
    }

    public String getBazName() {
        return bazName;
    }

    public BazEntity setBazName(String bazName) {
        this.bazName = bazName;
        return this;
    }

    public Integer getId() {
        return bazId;
    }

    public void setId(Integer id) {
        setBazId(id);
    }

}
