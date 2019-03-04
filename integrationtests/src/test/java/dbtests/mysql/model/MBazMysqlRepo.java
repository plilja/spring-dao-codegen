package dbtests.mysql.model;

import dbtests.framework.Column;
import dbtests.framework.CurrentUserProvider;
import dbtests.framework.Dao;
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
public class MBazMysqlRepo extends Dao<MBazMysql, Integer> {

    public static final Column.IntColumn<MBazMysql> COLUMN_ID = new Column.IntColumn<>("id", "id");

    public static final Column.DateTimeColumn<MBazMysql> COLUMN_CHANGED_AT = new Column.DateTimeColumn<>("changed_at", "changedAt");

    public static final Column.StringColumn<MBazMysql> COLUMN_CHANGED_BY = new Column.StringColumn<>("changed_by", "changedBy");

    public static final Column<MBazMysql, ColorEnumMysql> COLUMN_COLOR_ENUM_MYSQL_ID = new Column<>("color_enum_mysql_id", "colorEnumMysql", ColorEnumMysql.class);

    public static final Column.DateTimeColumn<MBazMysql> COLUMN_CREATED_AT = new Column.DateTimeColumn<>("created_at", "createdAt");

    public static final Column.StringColumn<MBazMysql> COLUMN_CREATED_BY = new Column.StringColumn<>("created_by", "createdBy");

    public static final Column.StringColumn<MBazMysql> COLUMN_NAME = new Column.StringColumn<>("name", "name");

    public static final Column.IntColumn<MBazMysql> COLUMN_VERSION = new Column.IntColumn<>("version", "version");

    public static final List<Column<MBazMysql, ?>> ALL_COLUMNS_LIST = Arrays.asList(
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

    private static final RowMapper<MBazMysql> ROW_MAPPER = (rs, i) -> {
        MBazMysql r = new MBazMysql();
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
    public MBazMysqlRepo(NamedParameterJdbcTemplate jdbcTemplate, CurrentUserProvider currentUserProvider) {
        super(Integer.class, true, jdbcTemplate, currentUserProvider);
    }

    @Override
    protected SqlParameterSource getParams(MBazMysql o) {
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
    protected RowMapper<MBazMysql> getRowMapper() {
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
    protected String getUpdateSql(MBazMysql object) {
        String updateSql = "UPDATE BazMysql SET " +
                "changed_at = :changed_at, " +
                "changed_by = :changed_by, " +
                "color_enum_mysql_id = :color_enum_mysql_id, " +
                "name = :name, " +
                "version = (IFNULL(:version, -1) + 1) %% 128 " +
                "WHERE id = :id %s";
        String versionClause;
        if (object.getVersion() != null) {
            versionClause = "AND (version = :version OR version IS NULL)";
        } else {
            versionClause = "";
        }
        return String.format(updateSql, versionClause);
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
    protected List<Column<MBazMysql, ?>> getColumnsList() {
        return ALL_COLUMNS_LIST;
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
    protected String getSelectAndLockSql(String databaseProductName) {
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
