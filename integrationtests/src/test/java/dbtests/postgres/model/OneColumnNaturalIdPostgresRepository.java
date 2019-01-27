package dbtests.postgres.model;

import dbtests.framework.AbstractBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class OneColumnNaturalIdPostgresRepository extends AbstractBaseRepository<OneColumnNaturalIdPostgresEntity, String> {

    private static final RowMapper<OneColumnNaturalIdPostgresEntity> ROW_MAPPER = (rs, i) -> {
        OneColumnNaturalIdPostgresEntity r = new OneColumnNaturalIdPostgresEntity();
        r.setId(rs.getString("id"));
        return r;
    };

    @Autowired
    public OneColumnNaturalIdPostgresRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(String.class, false, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(OneColumnNaturalIdPostgresEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId());
        return m;
    }

    @Override
    protected RowMapper<OneColumnNaturalIdPostgresEntity> getRowMapper() {
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
                "id " +
                "FROM test_schema.one_column_natural_id_postgres " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                "   id " +
                "FROM test_schema.one_column_natural_id_postgres " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                "id %n" +
                "FROM test_schema.one_column_natural_id_postgres %n" +
                "LIMIT %d OFFSET %d", pageSize, start);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO test_schema.one_column_natural_id_postgres (" +
                "   id" +
                ") " +
                "VALUES (" +
                "   :id" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return null;
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
    protected String getPrimaryKeyColumnName() {
        return "id";
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}
