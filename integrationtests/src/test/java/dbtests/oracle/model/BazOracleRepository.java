package dbtests.oracle.model;

import dbtests.framework.Dao;
import dbtests.framework.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class BazOracleRepository extends Dao<BazOracle, Integer> {

    private static final RowMapper<BazOracle> ROW_MAPPER = (rs, i) -> {
        BazOracle r = new BazOracle();
        r.setId(rs.getInt("ID"));
        r.setChangedAt(rs.getObject("CHANGED_AT") != null ? rs.getTimestamp("CHANGED_AT").toLocalDateTime() : null);
        r.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());
        r.setName(rs.getString("NAME"));
        r.setVersion(rs.getInt("VERSION"));
        return r;
    };
    private static final String ALL_COLUMNS = " ID, CHANGED_AT, CREATED_AT, NAME, VERSION ";

    @Autowired
    public BazOracleRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(BazOracle o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("ID", o.getId());
        m.addValue("CHANGED_AT", o.getChangedAt());
        m.addValue("CREATED_AT", o.getCreatedAt());
        m.addValue("NAME", o.getName());
        m.addValue("VERSION", o.getVersion());
        return m;
    }

    @Override
    protected RowMapper<BazOracle> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM DOCKER.BAZ_ORACLE " +
                "WHERE ID = :ID";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM DOCKER.BAZ_ORACLE " +
                "WHERE ID IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM DOCKER.BAZ_ORACLE " +
                "WHERE ROWNUM <= %d", maxSelectCount);
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT * FROM (%n" +
                "SELECT rownum tmp_rownum_, a.* %n" +
                "FROM (SELECT %n" +
                ALL_COLUMNS +
                "FROM DOCKER.BAZ_ORACLE %n" +
                "ORDER BY ID %n" +
                ") a %n" +
                "WHERE rownum < %d + %d %n" +
                ")%n" +
                "WHERE tmp_rownum_ >= %d", start + 1, pageSize, start + 1);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO DOCKER.BAZ_ORACLE (" +
                "CHANGED_AT, " +
                "CREATED_AT, " +
                "NAME, " +
                "VERSION" +
                ") " +
                "VALUES (" +
                ":CHANGED_AT, " +
                ":CREATED_AT, " +
                ":NAME, " +
                ":VERSION" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE DOCKER.BAZ_ORACLE SET " +
                "CHANGED_AT = :CHANGED_AT, " +
                "NAME = :NAME, " +
                "VERSION = MOD(VERSION + 1, 128) " +
                "WHERE ID = :ID AND VERSION = :VERSION";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM DOCKER.BAZ_ORACLE " +
                "WHERE ID IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM DOCKER.BAZ_ORACLE";
    }

    @Override
    protected String getLockSql() {
        return "SELECT ID FROM DOCKER.BAZ_ORACLE " +
                "WHERE ID = :id " +
                "FOR UPDATE";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "ID";
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}
