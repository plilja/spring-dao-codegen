package dbtests.oracle.tests;

import dbtests.BaseIntegrationTest;
import dbtests.oracle.model.BazOracle;
import dbtests.oracle.model.BazOracleRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ContextConfiguration(classes = {OracleITConfig.class})
@ExtendWith(SpringExtension.class)
public class OracleIT extends BaseIntegrationTest<BazOracle, BazOracleRepository> {

    @Autowired
    private
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private
    BazOracleRepository repo;

    @Override
    protected BazOracleRepository getRepo() {
        return repo;
    }

    @Override
    public void clearTable() {
        jdbcTemplate.update("DELETE FROM DOCKER.BAZ_ORACLE", new MapSqlParameterSource());
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
    protected LocalDateTime getCreatedAt(BazOracle entity) {
        return entity.getCreatedAt();
    }

    @Override
    protected LocalDateTime getChangedAt(BazOracle entity) {
        return entity.getChangedAt();
    }

    @Override
    protected Integer getVersion(BazOracle entity) {
        return entity.getVersion();
    }

    @Override
    protected void setVersion(BazOracle entity, Integer version) {
        entity.setVersion(version);
    }

    @Override
    protected void insertObjectWithoutVersionColumn(String name) {
        var params = new MapSqlParameterSource()
                .addValue("name", "Glenn")
                .addValue("created_at", LocalDateTime.now())
                .addValue("changed_at", LocalDateTime.now());
        jdbcTemplate.update("INSERT INTO DOCKER.BAZ_ORACLE (name, created_at, changed_at) values (:name, :created_at, :changed_at)", params);
    }

}
