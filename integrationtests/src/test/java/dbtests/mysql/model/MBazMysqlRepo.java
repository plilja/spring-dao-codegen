package dbtests.mysql.model;

import dbtests.framework.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class MBazMysqlRepo extends Dao<MBazMysql, Integer> {

    private static final RowMapper<MBazMysql> ROW_MAPPER = (rs, i) -> {
        MBazMysql r = new MBazMysql();
        r.setId(rs.getObject("id") != null ? rs.getInt("id") : null);
        r.setChangedAt(rs.getObject("changed_at") != null ? rs.getTimestamp("changed_at").toLocalDateTime() : null);
        r.setCreatedAt(rs.getObject("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        r.setName(rs.getString("name"));
        r.setVersion(rs.getObject("version") != null ? rs.getInt("version") : null);
        return r;
    };
    private static final String ALL_COLUMNS = " id, changed_at, created_at, `name`, version ";

    @Autowired
    public MBazMysqlRepo(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(MBazMysql o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId());
        m.addValue("changed_at", o.getChangedAt());
        m.addValue("created_at", o.getCreatedAt());
        m.addValue("name", o.getName());
        m.addValue("version", o.getVersion());
        return m;
    }

    @Override
    protected RowMapper<MBazMysql> getRowMapper() {
        return ROW_MAPPER;
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
                ALL_COLUMNS +
                "FROM BazMysql " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM BazMysql " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM BazMysql %n" +
                "LIMIT %d OFFSET %d", pageSize, start);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO BazMysql (" +
                "changed_at, " +
                "created_at, " +
                "`name`, " +
                "version" +
                ") " +
                "VALUES (" +
                ":changed_at, " +
                ":created_at, " +
                ":name, " +
                ":version" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE BazMysql SET " +
                "changed_at = :changed_at, " +
                "name = :name, " +
                "version = (version + 1) % 128 " +
                "WHERE id = :id AND version = :version";
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
