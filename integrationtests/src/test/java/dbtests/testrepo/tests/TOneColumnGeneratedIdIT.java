package dbtests.testrepo.tests;

import dbtests.OneColumnGeneratedIdBaseTest;
import dbtests.mssql.model.OneColumnGeneratedIdMsSqlEntity;
import dbtests.testrepo.model.TOneColumnGeneratedIdMsSqlRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@Import(TOneColumnGeneratedIdMsSqlRepository.class)
@RunWith(SpringRunner.class)
public class TOneColumnGeneratedIdIT extends OneColumnGeneratedIdBaseTest<OneColumnGeneratedIdMsSqlEntity, TOneColumnGeneratedIdMsSqlRepository> {

    @Autowired
    TOneColumnGeneratedIdMsSqlRepository repo;

    @Override
    protected TOneColumnGeneratedIdMsSqlRepository getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        repo.clear();
    }

    @Override
    protected OneColumnGeneratedIdMsSqlEntity newEntity() {
        return new OneColumnGeneratedIdMsSqlEntity();
    }
}
