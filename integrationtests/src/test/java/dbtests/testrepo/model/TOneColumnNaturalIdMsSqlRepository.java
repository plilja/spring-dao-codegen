package dbtests.testrepo.model;

import dbtests.mssql.model.OneColumnNaturalIdMsSqlEntity;
import dbtests.mssql.model.OneColumnNaturalIdMsSqlRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TOneColumnNaturalIdMsSqlRepository extends BaseTestRepository<OneColumnNaturalIdMsSqlEntity, String> implements OneColumnNaturalIdMsSqlRepository {

    public TOneColumnNaturalIdMsSqlRepository() {
        super(false, 10, false);
    }

}
