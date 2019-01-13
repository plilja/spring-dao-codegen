package se.plilja.springdaogen.dbtests.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import se.plilja.springdaogen.dbtests.framework.BaseRepository;

@Repository
public class BazMysqlRepository extends BaseRepository<BazMysqlEntity, Integer> {

    private static final RowMapper<BazMysqlEntity> ROW_MAPPER = (rs, i) -> {
        return new BazMysqlEntity()
                .setId(rs.getObject("id") != null ? rs.getInt("id") : null)
                .setName(rs.getString("name"));
    };

    @Autowired
    public BazMysqlRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, jdbcTemplate, ROW_MAPPER);
    }

    @Override
    public SqlParameterSource getParams(BazMysqlEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId());
        m.addValue("name", o.getName());
        return m;
    }

    @Override
    protected String getSelectOneSql() {
        return "SELECT " +
                "   id, " +
                "   name " +
                "FROM BazMysql " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                "   id, " +
                "   name " +
                "FROM BazMysql " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO BazMysql (" +
                "   name" +
                ") " +
                "VALUES (" +
                "   :name" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE BazMysql SET " +
                "   name = :name " +
                "WHERE id = :id";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "id";
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}
