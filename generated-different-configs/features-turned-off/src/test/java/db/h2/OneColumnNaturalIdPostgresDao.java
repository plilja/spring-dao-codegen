package db.h2;

import java.sql.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class OneColumnNaturalIdPostgresDao extends Dao<OneColumnNaturalIdPostgres, String> {

    private static final String ALL_COLUMNS = " id ";

    private static final RowMapper<OneColumnNaturalIdPostgres> ROW_MAPPER = (rs, i) -> {
        OneColumnNaturalIdPostgres r = new OneColumnNaturalIdPostgres();
        r.setId(rs.getString("id"));
        return r;
    };

    @Autowired
    public OneColumnNaturalIdPostgresDao(NamedParameterJdbcTemplate jdbcTemplate) {
        super(String.class, false, jdbcTemplate);
    }

    @Override
    protected SqlParameterSource getParams(OneColumnNaturalIdPostgres o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId(), Types.VARCHAR);
        return m;
    }

    @Override
    protected RowMapper<OneColumnNaturalIdPostgres> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM test_schema.one_column_natural_id_postgres " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.one_column_natural_id_postgres " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.one_column_natural_id_postgres " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO test_schema.one_column_natural_id_postgres (" +
                "id" +
                ") " +
                "VALUES (" +
                ":id" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM test_schema.one_column_natural_id_postgres " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM test_schema.one_column_natural_id_postgres";
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM test_schema.one_column_natural_id_postgres %n" +
                "ORDER BY id " +
                "LIMIT %d OFFSET %d", pageSize, start);
    }

    @Override
    protected String getSelectAndLockSql(String databaseProductName) {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.one_column_natural_id_postgres " +
                "WHERE id = :id " +
                "FOR UPDATE";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "id";
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 1000;
    }

}
