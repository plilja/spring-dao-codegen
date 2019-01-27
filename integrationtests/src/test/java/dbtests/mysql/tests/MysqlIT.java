package dbtests.mysql.tests;


import dbtests.BaseIntegrationTest;
import dbtests.mysql.model.BazMysqlEntity;
import dbtests.mysql.model.BazMysqlRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {MysqlITConfig.class})
@Import(BazMysqlRepository.class)
@RunWith(SpringRunner.class)
public class MysqlIT extends BaseIntegrationTest<BazMysqlEntity, BazMysqlRepository> {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    BazMysqlRepository repo;

    @Override
    protected BazMysqlRepository getRepo() {
        return repo;
    }

    @Override
    public void clearTable() {
        jdbcTemplate.update("DELETE FROM BazMysql", new MapSqlParameterSource());
    }

    @Override
    protected BazMysqlEntity newEntity(String name) {
        return new BazMysqlEntity(null, name);
    }

    @Override
    protected String getName(BazMysqlEntity entity) {
        return entity.getName();
    }

}

