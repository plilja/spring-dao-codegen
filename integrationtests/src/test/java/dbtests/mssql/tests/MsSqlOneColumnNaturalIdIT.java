package dbtests.mssql.tests;

import dbtests.OneColumnNaturalIdBaseTest;
import dbtests.mssql.model.OneColumnNaturalIdMsSqlEntity;
import dbtests.mssql.model.OneColumnNaturalIdMsSqlRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {MsSqlITConfig.class})
@ExtendWith(SpringExtension.class)
public class MsSqlOneColumnNaturalIdIT extends OneColumnNaturalIdBaseTest<OneColumnNaturalIdMsSqlEntity, OneColumnNaturalIdMsSqlRepository> {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private OneColumnNaturalIdMsSqlRepository repo;

    @Override
    protected OneColumnNaturalIdMsSqlRepository getRepo() {
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
