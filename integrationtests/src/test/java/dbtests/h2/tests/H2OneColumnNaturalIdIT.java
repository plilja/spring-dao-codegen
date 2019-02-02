package dbtests.h2.tests;

import dbtests.OneColumnNaturalIdBaseTest;
import dbtests.h2.model.OneColumnNaturalIdH2;
import dbtests.h2.model.OneColumnNaturalIdH2Repo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {H2Config.class})
@ExtendWith(SpringExtension.class)
public class H2OneColumnNaturalIdIT extends OneColumnNaturalIdBaseTest<OneColumnNaturalIdH2, OneColumnNaturalIdH2Repo> {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private OneColumnNaturalIdH2Repo repo;

    @Override
    protected OneColumnNaturalIdH2Repo getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM test_schema.one_column_natural_id_h2", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnNaturalIdH2 newEntity() {
        return new OneColumnNaturalIdH2();
    }
}
