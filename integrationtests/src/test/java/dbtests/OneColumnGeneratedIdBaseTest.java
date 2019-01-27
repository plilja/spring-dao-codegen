package dbtests;

import dbtests.framework.BaseEntity;
import dbtests.framework.BaseRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class OneColumnGeneratedIdBaseTest<Entity extends BaseEntity<Integer>, Repo extends BaseRepository<Entity, Integer>> {
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
        getRepo().save(entity);
        assertNotNull(entity.getId());

        Entity retrievedEntity = getRepo().getOne(entity.getId());
        assertNotNull(retrievedEntity);
        assertEquals(entity.getId(), retrievedEntity.getId());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void updatesAreNotSupported() {
        Entity entity = newEntity();
        getRepo().save(entity);
        getRepo().save(entity);
    }

}
