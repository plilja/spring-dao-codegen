package dbtests.testrepo.model;

import dbtests.mssql.model.BazMsSqlEntity;
import dbtests.mssql.model.BazMsSqlRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TBazMsSqlRepository extends BaseTestRepository<BazMsSqlEntity, Integer> implements BazMsSqlRepository {

    public TBazMsSqlRepository() {
        super(true, 10, true);
    }

}
