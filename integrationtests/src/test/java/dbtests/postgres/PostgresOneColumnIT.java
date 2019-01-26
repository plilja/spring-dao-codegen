package dbtests.postgres;

import dbtests.OneColumnBaseTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {PostgresITConfig.class})
@Import(OneColumnPostgresRepository.class)
@RunWith(SpringRunner.class)
public class PostgresOneColumnIT extends OneColumnBaseTest<OneColumnPostgresEntity, OneColumnPostgresRepository> {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    OneColumnPostgresRepository repo;

    @Override
    protected OneColumnPostgresRepository getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM test_schema.one_column_postgres", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnPostgresEntity newEntity() {
        return new OneColumnPostgresEntity();
    }
}
