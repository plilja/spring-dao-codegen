package dbtests.postgres.tests;

import dbtests.OneColumnNaturalIdBaseTest;
import dbtests.postgres.model.OneColumnNaturalIdPostgresEntity;
import dbtests.postgres.model.OneColumnNaturalIdPostgresRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {PostgresITConfig.class})
@Import(OneColumnNaturalIdPostgresRepository.class)
@RunWith(SpringRunner.class)
public class PostgresOneColumnNaturalIdIT extends OneColumnNaturalIdBaseTest<OneColumnNaturalIdPostgresEntity, OneColumnNaturalIdPostgresRepository> {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    OneColumnNaturalIdPostgresRepository repo;

    @Override
    protected OneColumnNaturalIdPostgresRepository getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM test_schema.one_column_natural_id_postgres", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnNaturalIdPostgresEntity newEntity() {
        return new OneColumnNaturalIdPostgresEntity();
    }
}
