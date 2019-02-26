package db.h2;

import java.sql.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class ColorEnumPostgresDao extends Dao<ColorEnumPostgres, String> {

    private static final String ALL_COLUMNS = " id, hex ";

    private static final RowMapper<ColorEnumPostgres> ROW_MAPPER = (rs, i) -> {
        ColorEnumPostgres r = new ColorEnumPostgres();
        r.setId(rs.getString("id"));
        r.setHex(rs.getString("hex"));
        return r;
    };

    @Autowired
    public ColorEnumPostgresDao(NamedParameterJdbcTemplate jdbcTemplate) {
        super(String.class, false, jdbcTemplate);
    }

    @Override
    protected SqlParameterSource getParams(ColorEnumPostgres o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId(), Types.VARCHAR);
        m.addValue("hex", o.getHex(), Types.VARCHAR);
        return m;
    }

    @Override
    protected RowMapper<ColorEnumPostgres> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM test_schema.color_enum_postgres " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.color_enum_postgres " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.color_enum_postgres " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO test_schema.color_enum_postgres (" +
                "id, " +
                "hex" +
                ") " +
                "VALUES (" +
                ":id, " +
                ":hex" +
                ")";
    }

    @Override
    protected String getUpdateSql(ColorEnumPostgres object) {
        return "UPDATE test_schema.color_enum_postgres SET " +
                "hex = :hex " +
                "WHERE id = :id";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM test_schema.color_enum_postgres " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM test_schema.color_enum_postgres";
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM test_schema.color_enum_postgres %n" +
                "ORDER BY id " +
                "LIMIT %d OFFSET %d", pageSize, start);
    }

    @Override
    protected String getSelectAndLockSql(String databaseProductName) {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.color_enum_postgres " +
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
