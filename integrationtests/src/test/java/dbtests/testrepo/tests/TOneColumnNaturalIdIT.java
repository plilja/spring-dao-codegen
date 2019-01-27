package dbtests.testrepo.tests;

import dbtests.OneColumnNaturalIdBaseTest;
import dbtests.mssql.model.OneColumnNaturalIdMsSqlEntity;
import dbtests.testrepo.model.TOneColumnNaturalIdMsSqlRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@Import(TOneColumnNaturalIdMsSqlRepository.class)
@RunWith(SpringRunner.class)
public class TOneColumnNaturalIdIT extends OneColumnNaturalIdBaseTest<OneColumnNaturalIdMsSqlEntity, TOneColumnNaturalIdMsSqlRepository> {
    @Autowired
    TOneColumnNaturalIdMsSqlRepository repo;

    @Override
    protected TOneColumnNaturalIdMsSqlRepository getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        repo.clear();
    }

    @Override
    protected OneColumnNaturalIdMsSqlEntity newEntity() {
        return new OneColumnNaturalIdMsSqlEntity();
    }
}
