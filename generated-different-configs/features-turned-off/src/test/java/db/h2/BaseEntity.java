package db.h2;

public interface BaseEntity<ID> {
    void setId(ID id);

    ID getId();

}