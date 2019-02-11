package dbtests.mysql.model;

import dbtests.framework.Column;
import dbtests.framework.Dao;
import dbtests.framework.DatabaseException;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class MBazMysqlRepo extends Dao<MBazMysql, Integer> {

    public static final Column<MBazMysql, Integer> COLUMN_ID = new Column<>("id");

    public static final Column<MBazMysql, LocalDateTime> COLUMN_CHANGED_AT = new Column<>("changed_at");

    public static final Column<MBazMysql, ColorEnumMysql> COLUMN_COLOR_ENUM_MYSQL_ID = new Column<>("color_enum_mysql_id");

    public static final Column<MBazMysql, LocalDateTime> COLUMN_CREATED_AT = new Column<>("created_at");

    public static final Column<MBazMysql, String> COLUMN_NAME = new Column<>("name");

    public static final Column<MBazMysql, Integer> COLUMN_VERSION = new Column<>("version");

    private static final String ALL_COLUMNS = " id, changed_at, color_enum_mysql_id, created_at, name, " +
            " version ";

    private static final RowMapper<MBazMysql> ROW_MAPPER = (rs, i) -> {
        MBazMysql r = new MBazMysql();
        r.setId(rs.getInt("id"));
        r.setChangedAt(rs.getTimestamp("changed_at").toLocalDateTime());
        r.setColorEnumMysql(ColorEnumMysql.fromId(rs.getObject("color_enum_mysql_id") != null ? rs.getInt("color_enum_mysql_id") : null));
        r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        r.setName(rs.getString("name"));
        r.setVersion(rs.getObject("version") != null ? rs.getInt("version") : null);
        return r;
    };

    @Autowired
    public MBazMysqlRepo(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate);
    }

    @Override
    protected SqlParameterSource getParams(MBazMysql o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId());
        m.addValue("changed_at", o.getChangedAt());
        m.addValue("color_enum_mysql_id", o.getColorEnumMysql() != null ? o.getColorEnumMysql().getId() : null);
        m.addValue("created_at", o.getCreatedAt());
        m.addValue("name", o.getName());
        m.addValue("version", o.getVersion());
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
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM BazMysql %n" +
                "LIMIT %d OFFSET %d", pageSize, start);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO BazMysql (" +
                "changed_at, " +
                "color_enum_mysql_id, " +
                "created_at, " +
                "name, " +
                "version" +
                ") " +
                "VALUES (" +
                ":changed_at, " +
                ":color_enum_mysql_id, " +
                ":created_at, " +
                ":name, " +
                ":version" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE BazMysql SET " +
                "changed_at = :changed_at, " +
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
    protected String getQuerySql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM BazMysql " +
                "WHERE %s " +
                "LIMIT %d";
    }

    @Override
    protected String getLockSql() {
        return "SELECT id FROM BazMysql " +
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
