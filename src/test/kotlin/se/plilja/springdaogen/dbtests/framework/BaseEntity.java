package se.plilja.springdaogen.dbtests.framework;

public interface BaseEntity<SELF, ID> {
    SELF setId(ID id);

    ID getId();

}