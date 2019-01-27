package dbtests.mssql.tests;

import dbtests.OneColumnGeneratedIdBaseTest;
import dbtests.mssql.model.OneColumnGeneratedIdMsSqlEntity;
import dbtests.mssql.model.OneColumnGeneratedIdMsSqlRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {MsSqlITConfig.class})
@Import(OneColumnGeneratedIdMsSqlRepository.class)
@RunWith(SpringRunner.class)
public class MsSqlOneColumnGeneratedIdIT extends OneColumnGeneratedIdBaseTest<OneColumnGeneratedIdMsSqlEntity, OneColumnGeneratedIdMsSqlRepository> {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    OneColumnGeneratedIdMsSqlRepository repo;

    @Override
    protected OneColumnGeneratedIdMsSqlRepository getRepo() {
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
