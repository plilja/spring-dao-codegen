package dbtests.framework;

public interface BaseEntity<ID> {
    void setId(ID id);

    ID getId();

}