package dbtests.h2.mssql;

import dbtests.OneColumnNaturalIdBaseTest;
import dbtests.mssql.model.OneColumnNaturalIdMsSqlDao;
import dbtests.mssql.model.OneColumnNaturalIdMsSqlEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {H2MsSqlITConfig.class})
@ExtendWith(SpringExtension.class)
public class H2MsSqlOneColumnNaturalIdIT extends OneColumnNaturalIdBaseTest<OneColumnNaturalIdMsSqlEntity, OneColumnNaturalIdMsSqlDao> {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private OneColumnNaturalIdMsSqlDao repo;

    @Override
    protected OneColumnNaturalIdMsSqlDao getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM dbo.ONE_COLUMN_NATURAL_ID_MS_SQL", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnNaturalIdMsSqlEntity newEntity() {
        return new OneColumnNaturalIdMsSqlEntity();
    }
}
