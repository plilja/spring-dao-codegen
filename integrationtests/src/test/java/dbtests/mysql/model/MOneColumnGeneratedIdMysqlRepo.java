package dbtests.mysql.model;

import dbtests.framework.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class MOneColumnGeneratedIdMysqlRepo extends BaseRepository<MOneColumnGeneratedIdMysql, Integer> {

    private static final RowMapper<MOneColumnGeneratedIdMysql> ROW_MAPPER = (rs, i) -> {
        MOneColumnGeneratedIdMysql r = new MOneColumnGeneratedIdMysql();
        r.setId(rs.getObject("id") != null ? rs.getInt("id") : null);
        return r;
    };

    @Autowired
    public MOneColumnGeneratedIdMysqlRepo(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate, ROW_MAPPER);
    }

    @Override
    public SqlParameterSource getParams(MOneColumnGeneratedIdMysql o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId());
        return m;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM ONE_COLUMN_GENERATED_ID_MYSQL " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                "id " +
                "FROM ONE_COLUMN_GENERATED_ID_MYSQL " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                "   id " +
                "FROM ONE_COLUMN_GENERATED_ID_MYSQL " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                "id %n" +
                "FROM ONE_COLUMN_GENERATED_ID_MYSQL %n" +
                "LIMIT %d OFFSET %d", pageSize, start);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO ONE_COLUMN_GENERATED_ID_MYSQL() VALUES()";
    }

    @Override
    protected String getUpdateSql() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM ONE_COLUMN_GENERATED_ID_MYSQL " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM ONE_COLUMN_GENERATED_ID_MYSQL";
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
