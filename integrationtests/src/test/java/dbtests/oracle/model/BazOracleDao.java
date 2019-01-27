package dbtests.oracle.model;

import dbtests.framework.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class BazOracleDao extends BaseRepository<BazOracle, Integer> {

    private static final RowMapper<BazOracle> ROW_MAPPER = (rs, i) -> {
        BazOracle r = new BazOracle();
        r.setId(rs.getObject("ID") != null ? rs.getInt("ID") : null);
        r.setName(rs.getString("NAME"));
        return r;
    };

    @Autowired
    public BazOracleDao(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate, ROW_MAPPER);
    }

    @Override
    public SqlParameterSource getParams(BazOracle o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("ID", o.getId());
        m.addValue("NAME", o.getName());
        return m;
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
                "ID, " +
                "NAME " +
                "FROM DOCKER.BAZ_ORACLE " +
                "WHERE ID IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                "   ID, " +
                "   NAME " +
                "FROM DOCKER.BAZ_ORACLE " +
                "WHERE ROWNUM <= %d", maxSelectCount);
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT * FROM (%n" +
                "SELECT rownum tmp_rownum_, a.* %n" +
                "FROM (SELECT %n" +
                "ID, %n" +
                "NAME %n" +
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
                "   NAME" +
                ") " +
                "VALUES (" +
                "   :NAME" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE DOCKER.BAZ_ORACLE SET " +
                "   NAME = :NAME " +
                "WHERE ID = :ID";
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
    protected String getPrimaryKeyColumnName() {
        return "ID";
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}
