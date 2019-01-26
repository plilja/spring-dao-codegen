package dbtests.postgres;

import dbtests.framework.BaseEntity;

public class OneColumnPostgresEntity implements BaseEntity<Integer> {

    private Integer id;

    public OneColumnPostgresEntity() {
    }

    public OneColumnPostgresEntity(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

}
