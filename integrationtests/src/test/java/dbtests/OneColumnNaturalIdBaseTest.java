package dbtests;

import dbtests.framework.BaseEntity;
import dbtests.framework.BaseRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class OneColumnNaturalIdBaseTest<Entity extends BaseEntity<String>, Repo extends BaseRepository<Entity, String>> {
    @Before
    public void before() {
        clearTable();
    }

    protected abstract Repo getRepo();

    protected abstract void clearTable();

    protected abstract Entity newEntity();

    @Test
    public void create() {
        Entity entity = newEntity();
        entity.setId("Ole");
        getRepo().save(entity);

        Entity retrievedEntity = getRepo().getOne("Ole");
        assertNotNull(retrievedEntity);
        assertEquals("Ole", retrievedEntity.getId());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void updatesAreNotSupported() {
        Entity entity = newEntity();
        entity.setId("Ole");
        getRepo().save(entity);
        getRepo().save(entity);
    }

}
