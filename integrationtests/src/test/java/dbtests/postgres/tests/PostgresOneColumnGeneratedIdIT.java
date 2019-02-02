package dbtests.postgres.tests;

import dbtests.OneColumnGeneratedIdBaseTest;
import dbtests.postgres.model.OneColumnGeneratedIdPostgresEntity;
import dbtests.postgres.model.OneColumnGeneratedIdPostgresRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PostgresITConfig.class})
@ExtendWith(SpringExtension.class)
public class PostgresOneColumnGeneratedIdIT extends OneColumnGeneratedIdBaseTest<OneColumnGeneratedIdPostgresEntity, OneColumnGeneratedIdPostgresRepository> {
    @Autowired
    private
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private
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
