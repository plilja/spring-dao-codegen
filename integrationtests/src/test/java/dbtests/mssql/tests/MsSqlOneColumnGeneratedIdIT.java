package dbtests.mssql.tests;

import dbtests.OneColumnGeneratedIdBaseTest;
import dbtests.mssql.model.OneColumnGeneratedIdMsSqlEntity;
import dbtests.mssql.model.OneColumnGeneratedIdMsSqlRepository;
import dbtests.mssql.model.OneColumnGeneratedIdMsSqlRepositoryImpl;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {MsSqlITConfig.class})
@Import(OneColumnGeneratedIdMsSqlRepositoryImpl.class)
@RunWith(SpringRunner.class)
public class MsSqlOneColumnGeneratedIdIT extends OneColumnGeneratedIdBaseTest<OneColumnGeneratedIdMsSqlEntity, OneColumnGeneratedIdMsSqlRepository> {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    OneColumnGeneratedIdMsSqlRepositoryImpl repo;

    @Override
    protected OneColumnGeneratedIdMsSqlRepositoryImpl getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM one_column_generated_id_ms_sql", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnGeneratedIdMsSqlEntity newEntity() {
        return new OneColumnGeneratedIdMsSqlEntity();
    }
}
