package dbtests.testrepo.model;

import dbtests.mssql.model.OneColumnGeneratedIdMsSqlEntity;
import dbtests.mssql.model.OneColumnGeneratedIdMsSqlRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TOneColumnGeneratedIdMsSqlRepository extends BaseTestRepository<OneColumnGeneratedIdMsSqlEntity, Integer> implements OneColumnGeneratedIdMsSqlRepository {

    public TOneColumnGeneratedIdMsSqlRepository() {
        super(true, 10, false);
    }

}
