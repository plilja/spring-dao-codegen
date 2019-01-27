package dbtests.testrepo.tests;

import dbtests.mssql.model.BazMsSqlEntity;
import dbtests.testrepo.model.TBazMsSqlRepository;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestRepoTest {
    private TBazMsSqlRepository repo = new TBazMsSqlRepository();

    @Test
    public void changeToAPersistedObject() {
        BazMsSqlEntity entity = new BazMsSqlEntity();
        entity.setName("Foo");

        // when
        repo.save(entity);
        BazMsSqlEntity retrieved = repo.getOne(entity.getId());
        entity.setName("Bar");

        // then
        assertEquals("Bar", entity.getName());
        assertEquals("Foo", retrieved.getName());
    }
}
