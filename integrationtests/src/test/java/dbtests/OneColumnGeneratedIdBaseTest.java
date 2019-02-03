package dbtests;

import dbtests.framework.BaseEntity;
import dbtests.framework.Dao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public abstract class OneColumnGeneratedIdBaseTest<Entity extends BaseEntity<Integer>, Repo extends Dao<Entity, Integer>> {
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
        getRepo().save(entity);
        assertNotNull(entity.getId());

        Entity retrievedEntity = getRepo().getOne(entity.getId());
        assertNotNull(retrievedEntity);
        assertEquals(entity.getId(), retrievedEntity.getId());
    }

    @Test
    void updatesAreNotSupported() {
        assertThrows(UnsupportedOperationException.class, () -> {
            Entity entity = newEntity();
            getRepo().save(entity);
            getRepo().save(entity);
        });
    }

}
