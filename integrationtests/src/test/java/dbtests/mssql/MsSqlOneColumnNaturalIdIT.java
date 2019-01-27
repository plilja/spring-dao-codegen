package dbtests.mssql;

import dbtests.OneColumnNaturalIdBaseTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {MsSqlITConfig.class})
@Import(OneColumnNaturalIdMsSqlRepository.class)
@RunWith(SpringRunner.class)
public class MsSqlOneColumnNaturalIdIT extends OneColumnNaturalIdBaseTest<OneColumnNaturalIdMsSqlEntity, OneColumnNaturalIdMsSqlRepository> {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    OneColumnNaturalIdMsSqlRepository repo;

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
