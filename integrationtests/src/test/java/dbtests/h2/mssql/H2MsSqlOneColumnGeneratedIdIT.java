package dbtests.h2.mssql;

import dbtests.OneColumnGeneratedIdBaseTest;
import dbtests.mssql.model.OneColumnGeneratedIdMsSqlDao;
import dbtests.mssql.model.OneColumnGeneratedIdMsSqlEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {H2MsSqlITConfig.class})
@ExtendWith(SpringExtension.class)
public class H2MsSqlOneColumnGeneratedIdIT extends OneColumnGeneratedIdBaseTest<OneColumnGeneratedIdMsSqlEntity, OneColumnGeneratedIdMsSqlDao> {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private OneColumnGeneratedIdMsSqlDao repo;

    @Override
    protected OneColumnGeneratedIdMsSqlDao getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM dbo.one_column_generated_id_ms_sql", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnGeneratedIdMsSqlEntity newEntity() {
        return new OneColumnGeneratedIdMsSqlEntity();
    }
}
