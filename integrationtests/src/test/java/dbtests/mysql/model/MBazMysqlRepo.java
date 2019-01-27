package dbtests.mysql.model;

import dbtests.framework.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class MBazMysqlRepo extends BaseRepository<MBazMysql, Integer> {

    private static final RowMapper<MBazMysql> ROW_MAPPER = (rs, i) -> {
        MBazMysql r = new MBazMysql();
        r.setId(rs.getObject("id") != null ? rs.getInt("id") : null);
        r.setName(rs.getString("name"));
        return r;
    };

    @Autowired
    public MBazMysqlRepo(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate, ROW_MAPPER);
    }

    @Override
    public SqlParameterSource getParams(MBazMysql o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId());
        m.addValue("name", o.getName());
        return m;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM BazMysql " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                "id, " +
                "name " +
                "FROM BazMysql " +
                "WHERE id IN (:ids)";
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
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                "id, %n" +
                "name %n" +
                "FROM BazMysql %n" +
                "LIMIT %d OFFSET %d", pageSize, start);
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
    protected String getDeleteSql() {
        return "DELETE FROM BazMysql " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM BazMysql";
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
