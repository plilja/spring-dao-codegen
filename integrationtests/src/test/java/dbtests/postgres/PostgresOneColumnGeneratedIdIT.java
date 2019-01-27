package dbtests.postgres;

import dbtests.OneColumnGeneratedIdBaseTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {PostgresITConfig.class})
@Import(OneColumnGeneratedIdPostgresRepository.class)
@RunWith(SpringRunner.class)
public class PostgresOneColumnGeneratedIdIT extends OneColumnGeneratedIdBaseTest<OneColumnGeneratedIdPostgresEntity, OneColumnGeneratedIdPostgresRepository> {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    OneColumnGeneratedIdPostgresRepository repo;

    @Override
    protected OneColumnGeneratedIdPostgresRepository getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM test_schema.one_column_generated_id_postgres", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnGeneratedIdPostgresEntity newEntity() {
        return new OneColumnGeneratedIdPostgresEntity();
    }
}
