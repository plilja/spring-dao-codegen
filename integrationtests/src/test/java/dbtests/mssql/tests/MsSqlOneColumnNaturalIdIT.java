package dbtests.mssql.tests;

import dbtests.OneColumnNaturalIdBaseTest;
import dbtests.mssql.model.OneColumnNaturalIdMsSqlEntity;
import dbtests.mssql.model.OneColumnNaturalIdMsSqlRepository;
import dbtests.mssql.model.OneColumnNaturalIdMsSqlRepositoryImpl;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {MsSqlITConfig.class})
@Import(OneColumnNaturalIdMsSqlRepositoryImpl.class)
@RunWith(SpringRunner.class)
public class MsSqlOneColumnNaturalIdIT extends OneColumnNaturalIdBaseTest<OneColumnNaturalIdMsSqlEntity, OneColumnNaturalIdMsSqlRepository> {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    OneColumnNaturalIdMsSqlRepositoryImpl repo;

    @Override
    protected OneColumnNaturalIdMsSqlRepositoryImpl getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM ONE_COLUMN_NATURAL_ID_MS_SQL", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnNaturalIdMsSqlEntity newEntity() {
        return new OneColumnNaturalIdMsSqlEntity();
    }
}
