package dbtests.mysql.model;

import dbtests.framework.AbstractBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class MOneColumnNaturalIdMysqlRepo extends AbstractBaseRepository<MOneColumnNaturalIdMysql, String> {

    private static final RowMapper<MOneColumnNaturalIdMysql> ROW_MAPPER = (rs, i) -> {
        MOneColumnNaturalIdMysql r = new MOneColumnNaturalIdMysql();
        r.setId(rs.getString("id"));
        return r;
    };

    @Autowired
    public MOneColumnNaturalIdMysqlRepo(NamedParameterJdbcTemplate jdbcTemplate) {
        super(String.class, false, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(MOneColumnNaturalIdMysql o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId());
        return m;
    }

    @Override
    protected RowMapper<MOneColumnNaturalIdMysql> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM ONE_COLUMN_NATURAL_ID_MYSQL " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                "id " +
                "FROM ONE_COLUMN_NATURAL_ID_MYSQL " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                "   id " +
                "FROM ONE_COLUMN_NATURAL_ID_MYSQL " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                "id %n" +
                "FROM ONE_COLUMN_NATURAL_ID_MYSQL %n" +
                "LIMIT %d OFFSET %d", pageSize, start);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO ONE_COLUMN_NATURAL_ID_MYSQL (" +
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
        return "DELETE FROM ONE_COLUMN_NATURAL_ID_MYSQL " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM ONE_COLUMN_NATURAL_ID_MYSQL";
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
