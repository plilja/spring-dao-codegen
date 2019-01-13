package se.plilja.springdaogen.dbtests.postgres;

import se.plilja.springdaogen.dbtests.framework.BaseEntity;

public class BazPostgresEntity implements BaseEntity<BazPostgresEntity, Integer> {

    private Integer bazId;
    private String bazName;

    public BazPostgresEntity() {
    }

    public BazPostgresEntity(Integer bazId, String bazName) {
        this.bazId = bazId;
        this.bazName = bazName;
    }

    public String getBazName() {
        return bazName;
    }

    public BazPostgresEntity setBazName(String bazName) {
        this.bazName = bazName;
        return this;
    }

    public Integer getId() {
        return bazId;
    }

    public BazPostgresEntity setId(Integer id) {
        this.bazId = id;
        return this;
    }

}
