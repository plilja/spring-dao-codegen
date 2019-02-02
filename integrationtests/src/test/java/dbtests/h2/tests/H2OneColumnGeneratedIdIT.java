package dbtests.h2.tests;

import dbtests.OneColumnGeneratedIdBaseTest;
import dbtests.h2.model.OneColumnGeneratedIdH2;
import dbtests.h2.model.OneColumnGeneratedIdH2Repo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {H2Config.class})
@ExtendWith(SpringExtension.class)
public class H2OneColumnGeneratedIdIT extends OneColumnGeneratedIdBaseTest<OneColumnGeneratedIdH2, OneColumnGeneratedIdH2Repo> {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private OneColumnGeneratedIdH2Repo repo;

    @Override
    protected OneColumnGeneratedIdH2Repo getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM test_schema.one_column_generated_id_h2", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnGeneratedIdH2 newEntity() {
        return new OneColumnGeneratedIdH2();
    }
}
