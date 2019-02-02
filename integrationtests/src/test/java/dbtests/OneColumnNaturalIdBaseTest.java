package dbtests;

import dbtests.framework.BaseEntity;
import dbtests.framework.Dao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class OneColumnNaturalIdBaseTest<Entity extends BaseEntity<String>, Repo extends Dao<Entity, String>> {
    @BeforeEach
    void before() {
        clearTable();
    }

    protected abstract Repo getRepo();

    protected abstract void clearTable();

    protected abstract Entity newEntity();

    @Test
    void create() {
        Entity entity = newEntity();
        entity.setId("Ole");
        getRepo().create(entity);

        Entity retrievedEntity = getRepo().getOne("Ole");
        assertNotNull(retrievedEntity);
        assertEquals("Ole", retrievedEntity.getId());
    }

    @Test
    void updatesAreNotSupported() {
        assertThrows(UnsupportedOperationException.class, () -> {
            Entity entity = newEntity();
            entity.setId("Ole");
            getRepo().create(entity);
            getRepo().save(entity);
        });
    }

}
