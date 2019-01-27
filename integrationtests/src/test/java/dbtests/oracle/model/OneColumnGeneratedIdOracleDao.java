package dbtests.oracle.model;

import dbtests.framework.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class OneColumnGeneratedIdOracleDao extends BaseRepository<OneColumnGeneratedIdOracle, Integer> {

    private static final RowMapper<OneColumnGeneratedIdOracle> ROW_MAPPER = (rs, i) -> {
        OneColumnGeneratedIdOracle r = new OneColumnGeneratedIdOracle();
        r.setId(rs.getObject("ID") != null ? rs.getInt("ID") : null);
        return r;
    };

    @Autowired
    public OneColumnGeneratedIdOracleDao(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(OneColumnGeneratedIdOracle o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("ID", o.getId());
        return m;
    }

    @Override
    protected RowMapper<OneColumnGeneratedIdOracle> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM DOCKER.ONE_COLUMN_GENERATED_ID_ORACLE " +
                "WHERE ID = :ID";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                "ID " +
                "FROM DOCKER.ONE_COLUMN_GENERATED_ID_ORACLE " +
                "WHERE ID IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                "   ID " +
                "FROM DOCKER.ONE_COLUMN_GENERATED_ID_ORACLE " +
                "WHERE ROWNUM <= %d", maxSelectCount);
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT * FROM (%n" +
                "SELECT rownum tmp_rownum_, a.* %n" +
                "FROM (SELECT %n" +
                "ID %n" +
                "FROM DOCKER.ONE_COLUMN_GENERATED_ID_ORACLE %n" +
                "ORDER BY ID %n" +
                ") a %n" +
                "WHERE rownum < %d + %d %n" +
                ")%n" +
                "WHERE tmp_rownum_ >= %d", start + 1, pageSize, start + 1);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO DOCKER.ONE_COLUMN_GENERATED_ID_ORACLE(ID) VALUES(null)";
    }

    @Override
    protected String getUpdateSql() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM DOCKER.ONE_COLUMN_GENERATED_ID_ORACLE " +
                "WHERE ID IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM DOCKER.ONE_COLUMN_GENERATED_ID_ORACLE";
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
