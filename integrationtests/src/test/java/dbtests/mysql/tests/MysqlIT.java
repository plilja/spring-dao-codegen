package dbtests.mysql.tests;


import dbtests.BaseIntegrationTest;
import dbtests.mysql.model.MBazMysql;
import dbtests.mysql.model.MBazMysqlRepo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {MysqlITConfig.class})
@ExtendWith(SpringExtension.class)
public class MysqlIT extends BaseIntegrationTest<MBazMysql, MBazMysqlRepo> {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private MBazMysqlRepo repo;

    @Override
    protected MBazMysqlRepo getRepo() {
        return repo;
    }

    @Override
    public void clearTable() {
        jdbcTemplate.update("DELETE FROM BazMysql", new MapSqlParameterSource());
    }

    @Override
    protected MBazMysql newEntity(String name) {
        return new MBazMysql(null, name);
    }

    @Override
    protected String getName(MBazMysql entity) {
        return entity.getName();
    }

}

