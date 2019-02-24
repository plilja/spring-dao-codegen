package dbtests.h2.postgres;

import dbtests.OneColumnNaturalIdBaseTest;
import dbtests.postgres.model.OneColumnNaturalIdPostgresDao;
import dbtests.postgres.model.OneColumnNaturalIdPostgresEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {H2PostgresConfig.class})
@ExtendWith(SpringExtension.class)
public class H2PostgresOneColumnNaturalIdIT extends OneColumnNaturalIdBaseTest<OneColumnNaturalIdPostgresEntity, OneColumnNaturalIdPostgresDao> {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private OneColumnNaturalIdPostgresDao repo;

    @Override
    protected OneColumnNaturalIdPostgresDao getRepo() {
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
