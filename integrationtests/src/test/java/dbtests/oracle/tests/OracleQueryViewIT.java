package dbtests.oracle.tests;

import dbtests.QueryViewTest;
import dbtests.framework.Column;
import dbtests.oracle.model.BazOracle;
import dbtests.oracle.model.BazOracleRepository;
import dbtests.oracle.model.BazViewOracle;
import dbtests.oracle.model.BazViewOracleRepository;
import dbtests.oracle.model.ColorEnumOracle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OracleITConfig.class})
@ExtendWith(SpringExtension.class)
public class OracleQueryViewIT extends QueryViewTest<BazViewOracle, BazViewOracleRepository> {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private BazOracleRepository repo;

    @Autowired
    private BazViewOracleRepository viewRepo;

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM DOCKER.BAZ_ORACLE", new MapSqlParameterSource());

    }

    @Override
    protected void saveNew(String name, String color) {
        BazOracle o = new BazOracle();
        o.setName(name);
        o.setColor(ColorEnumOracle.valueOf(color));
        repo.save(o);
    }

    @Override
    protected String getName(BazViewOracle o) {
        return o.getName();
    }

    @Override
    protected String getColor(BazViewOracle o) {
        return ColorEnumOracle.fromId(o.getColor()).name();
    }

    @Override
    protected BazViewOracleRepository getRepo() {
        return viewRepo;
    }

    @Override
    protected Column<BazViewOracle, String> getNameColumn() {
        return BazViewOracleRepository.COLUMN_NAME;
    }
}
