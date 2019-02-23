package db;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class BazMysqlH2Dao extends Dao<BazMysqlH2, Integer> {

    public static final Column<BazMysqlH2, Integer> COLUMN_ID = new Column<>("id");

    public static final Column<BazMysqlH2, LocalDateTime> COLUMN_CHANGED_AT = new Column<>("changed_at");

    public static final Column<BazMysqlH2, String> COLUMN_CHANGED_BY = new Column<>("changed_by");

    public static final Column<BazMysqlH2, ColorEnumMysql> COLUMN_COLOR_ENUM_MYSQL_ID = new Column<>("color_enum_mysql_id");

    public static final Column<BazMysqlH2, LocalDateTime> COLUMN_CREATED_AT = new Column<>("created_at");

    public static final Column<BazMysqlH2, String> COLUMN_CREATED_BY = new Column<>("created_by");

    public static final Column<BazMysqlH2, String> COLUMN_NAME = new Column<>("name");

    public static final Column<BazMysqlH2, Integer> COLUMN_VERSION = new Column<>("version");

    public static final List<Column<BazMysqlH2, ?>> ALL_COLUMNS_LIST = Arrays.asList(
            COLUMN_ID,
            COLUMN_CHANGED_AT,
            COLUMN_CHANGED_BY,
            COLUMN_COLOR_ENUM_MYSQL_ID,
            COLUMN_CREATED_AT,
            COLUMN_CREATED_BY,
            COLUMN_NAME,
            COLUMN_VERSION);

    private static final String ALL_COLUMNS = " id, changed_at, changed_by, color_enum_mysql_id, created_at, " +
            " created_by, name, version ";

    private static final RowMapper<BazMysqlH2> ROW_MAPPER = (rs, i) -> {
        BazMysqlH2 r = new BazMysqlH2();
        r.setId(rs.getInt("id"));
        r.setChangedAt(rs.getTimestamp("changed_at").toLocalDateTime());
        r.setChangedBy(rs.getString("changed_by"));
        r.setColorEnumMysql(ColorEnumMysql.fromId(rs.getObject("color_enum_mysql_id") != null ? rs.getInt("color_enum_mysql_id") : null));
        r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        r.setCreatedBy(rs.getString("created_by"));
        r.setName(rs.getString("name"));
        r.setVersion(rs.getObject("version") != null ? rs.getInt("version") : null);
        return r;
    };

    @Autowired
    public BazMysqlH2Dao(NamedParameterJdbcTemplate jdbcTemplate, CurrentUserProvider currentUserProvider) {
        super(Integer.class, true, jdbcTemplate, currentUserProvider);
    }

    @Override
    protected SqlParameterSource getParams(BazMysqlH2 o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId(), Types.INTEGER);
        m.addValue("changed_at", o.getChangedAt(), Types.TIMESTAMP);
        m.addValue("changed_by", o.getChangedBy(), Types.VARCHAR);
        m.addValue("color_enum_mysql_id", o.getColorEnumMysql() != null ? o.getColorEnumMysql().getId() : null, Types.INTEGER);
        m.addValue("created_at", o.getCreatedAt(), Types.TIMESTAMP);
        m.addValue("created_by", o.getCreatedBy(), Types.VARCHAR);
        m.addValue("name", o.getName(), Types.VARCHAR);
        m.addValue("version", o.getVersion(), Types.TINYINT);
        return m;
    }

    @Override
    protected RowMapper<BazMysqlH2> getRowMapper() {
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
    protected String getInsertSql() {
        return "INSERT INTO BazMysql (" +
                "changed_at, " +
                "changed_by, " +
                "color_enum_mysql_id, " +
                "created_at, " +
                "created_by, " +
                "name, " +
                "version" +
                ") " +
                "VALUES (" +
                ":changed_at, " +
                ":changed_by, " +
                ":color_enum_mysql_id, " +
                ":created_at, " +
                ":created_by, " +
                ":name, " +
                ":version" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE BazMysql SET " +
                "changed_at = :changed_at, " +
                "changed_by = :changed_by, " +
                "color_enum_mysql_id = :color_enum_mysql_id, " +
                "name = :name, " +
                "version = (IFNULL(:version, -1) + 1) % 128 " +
                "WHERE id = :id AND (version = :version OR version IS NULL OR :version IS NULL)";
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
    public Column<BazMysqlH2, ?> getColumnByName(String name) {
        for (Column<BazMysqlH2, ?> column : ALL_COLUMNS_LIST) {
            if (column.getName().equals(name)) {
                return column;
            }
        }
        return null;
    }

    @Override
    protected String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM BazMysql %n" +
                "WHERE 1=1 %s %n" +
                "%s " +
                "LIMIT %d", whereClause, orderBy, maxAllowedCount);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM BazMysql %n" +
                "WHERE 1=1 %s %n" +
                "%s %n" +
                "LIMIT %d OFFSET %d", whereClause, orderBy, pageSize, start);
    }

    @Override
    protected String getSelectAndLockSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM BazMysql " +
                "WHERE id = :id " +
                "FOR UPDATE";
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
