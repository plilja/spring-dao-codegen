package dbtests.mssql;

import dbtests.OneColumnBaseTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {MsSqlITConfig.class})
@Import(OneColumnMsSqlRepository.class)
@RunWith(SpringRunner.class)
public class MsSqlOneColumnIT extends OneColumnBaseTest<OneColumnMsSqlEntity, OneColumnMsSqlRepository> {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    OneColumnMsSqlRepository repo;

    @Override
    protected OneColumnMsSqlRepository getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM one_column_ms_sql", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnMsSqlEntity newEntity() {
        return new OneColumnMsSqlEntity();
    }
}
