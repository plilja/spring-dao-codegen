package db.h2;

import java.sql.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class ColorEnumH2Dao extends Dao<ColorEnumH2, String> {

    private static final String ALL_COLUMNS = " name, hex ";

    private static final RowMapper<ColorEnumH2> ROW_MAPPER = (rs, i) -> {
        ColorEnumH2 r = new ColorEnumH2();
        r.setName(rs.getString("name"));
        r.setHex(rs.getString("hex"));
        return r;
    };

    @Autowired
    public ColorEnumH2Dao(NamedParameterJdbcTemplate jdbcTemplate) {
        super(String.class, false, jdbcTemplate);
    }

    @Override
    protected SqlParameterSource getParams(ColorEnumH2 o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("name", o.getId(), Types.VARCHAR);
        m.addValue("hex", o.getHex(), Types.VARCHAR);
        return m;
    }

    @Override
    protected RowMapper<ColorEnumH2> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM test_schema.color_enum_h2 " +
                "WHERE name = :name";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.color_enum_h2 " +
                "WHERE name IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.color_enum_h2 " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO test_schema.color_enum_h2 (" +
                "name, " +
                "hex" +
                ") " +
                "VALUES (" +
                ":name, " +
                ":hex" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE test_schema.color_enum_h2 SET " +
                "hex = :hex " +
                "WHERE name = :name";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM test_schema.color_enum_h2 " +
                "WHERE name IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM test_schema.color_enum_h2";
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM test_schema.color_enum_h2 %n" +
                "ORDER BY name " +
                "LIMIT %d OFFSET %d", pageSize, start);
    }

    @Override
    protected String getSelectAndLockSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.color_enum_h2 " +
                "WHERE name = :id " +
                "FOR UPDATE";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "name";
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 1000;
    }

}
