package se.plilja.springdaogen.dbtests.framework;

public interface BaseEntity<ID> {
    void setId(ID id);

    ID getId();

}