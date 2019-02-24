package dbtests.h2.oracle;

import dbtests.QueryApiBaseTest;
import dbtests.framework.Column;
import dbtests.oracle.model.BazOracle;
import dbtests.oracle.model.BazOracleRepository;
import dbtests.oracle.model.ColorEnumOracle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {H2OracleITConfig.class})
@ExtendWith(SpringExtension.class)
class H2OracleQueryApiIT extends QueryApiBaseTest<BazOracle, BazOracleRepository> {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private BazOracleRepository repo;

    @Override
    protected BazOracleRepository getRepo() {
        return repo;
    }

    @Override
    public void clearTable() {
        jdbcTemplate.update("DELETE FROM DOCKER.BAZ_ORACLE", new MapSqlParameterSource());
    }

    @Override
    protected BazOracle newEntity(String name, String color) {
        BazOracle r = new BazOracle();
        r.setName(name);
        // TODO
        return r;
    }

    @Override
    protected BazOracle newEntity(String name) {
        BazOracle r = new BazOracle();
        r.setName(name);
        return r;
    }

    @Override
    protected String getName(BazOracle entity) {
        return entity.getName();
    }

    @Override
    protected void setName(BazOracle entity, String name) {
        entity.setName(name);
    }

    @Override
    protected void setColor(BazOracle entity, String color) {
        entity.setColor(getColorByName(color));
    }

    @Override
    protected ColorEnumOracle getColor(BazOracle entity) {
        return entity.getColor();
    }

    @Override
    protected ColorEnumOracle getColorByName(String color) {
        return ColorEnumOracle.fromId(color);
    }

    @Override
    protected Column<BazOracle, ?> getColorColumn() {
        return BazOracleRepository.COLUMN_COLOR;
    }

    @Override
    protected Column<BazOracle, Integer> getIdColumn() {
        return BazOracleRepository.COLUMN_ID;
    }

    @Override
    protected Column<BazOracle, String> getNameColumn() {
        return BazOracleRepository.COLUMN_NAME;
    }
}
