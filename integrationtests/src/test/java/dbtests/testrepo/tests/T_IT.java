package dbtests.testrepo.tests;

import dbtests.BaseIntegrationTest;
import dbtests.mssql.model.BazMsSqlEntity;
import dbtests.testrepo.model.TBazMsSqlRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@Import(TBazMsSqlRepository.class)
@RunWith(SpringRunner.class)
public class T_IT extends BaseIntegrationTest<BazMsSqlEntity, TBazMsSqlRepository> {

    @Autowired
    TBazMsSqlRepository repo;

    @Override
    protected TBazMsSqlRepository getRepo() {
        return repo;
    }

    @Override
    public void clearTable() {
        repo.clear();
    }

    @Override
    protected BazMsSqlEntity newEntity(String name) {
        return new BazMsSqlEntity(null, name);
    }

    @Override
    protected String getName(BazMsSqlEntity entity) {
        return entity.getName();
    }

}
