package se.plilja.springdaogen.dbtests.mssql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import se.plilja.springdaogen.dbtests.framework.BaseRepository;

@Repository
public class BazMsSqlRepository extends BaseRepository<BazMsSqlEntity, Integer> {

    private static final RowMapper<BazMsSqlEntity> ROW_MAPPER = (rs, i) -> {
        return new BazMsSqlEntity()
                .setId(rs.getObject("id") != null ? rs.getInt("id") : null)
                .setName(rs.getString("name"));
    };

    @Autowired
    public BazMsSqlRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, jdbcTemplate, ROW_MAPPER);
    }

    @Override
    public SqlParameterSource getParams(BazMsSqlEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId());
        m.addValue("name", o.getName());
        return m;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM dbo.baz_ms_sql " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectOneSql() {
        return "SELECT " +
                "   id, " +
                "   name " +
                "FROM dbo.baz_ms_sql " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT TOP %d " +
                "   id, " +
                "   name " +
                "FROM dbo.baz_ms_sql ", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO dbo.baz_ms_sql (" +
                "   name" +
                ") " +
                "VALUES (" +
                "   :name" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE dbo.baz_ms_sql SET " +
                "   name = :name " +
                "WHERE id = :id";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM dbo.baz_ms_sql " +
                "WHERE id IN (:ids)";
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
