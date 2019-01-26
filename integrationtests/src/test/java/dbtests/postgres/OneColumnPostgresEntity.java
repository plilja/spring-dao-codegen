package dbtests.postgres;

import dbtests.framework.BaseEntity;

public class OneColumnPostgresEntity implements BaseEntity<Integer> {

    private Integer id;

    public OneColumnPostgresEntity() {
    }

    public OneColumnPostgresEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
