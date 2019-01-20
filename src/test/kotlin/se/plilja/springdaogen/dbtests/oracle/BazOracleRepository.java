package se.plilja.springdaogen.dbtests.oracle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import se.plilja.springdaogen.dbtests.framework.BaseRepository;

@Repository
public class BazOracleRepository extends BaseRepository<BazOracleEntity, Integer> {

    private static final RowMapper<BazOracleEntity> ROW_MAPPER = (rs, i) -> {
        return new BazOracleEntity()
                .setId(rs.getObject("ID") != null ? rs.getInt("ID") : null)
                .setName(rs.getString("NAME"));
    };

    @Autowired
    public BazOracleRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, jdbcTemplate, ROW_MAPPER);
    }

    @Override
    public SqlParameterSource getParams(BazOracleEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("ID", o.getId());
        m.addValue("NAME", o.getName());
        return m;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM DOCKER.BAZ_ORACLE " +
                "WHERE ID = :ID";
    }

    @Override
    protected String getSelectOneSql() {
        return "SELECT " +
                "   ID, " +
                "   NAME " +
                "FROM DOCKER.BAZ_ORACLE " +
                "WHERE ID = :ID";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                "   ID, " +
                "   NAME " +
                "FROM DOCKER.BAZ_ORACLE " +
                "WHERE ROWNUM <= %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO DOCKER.BAZ_ORACLE (" +
                "   NAME" +
                ") " +
                "VALUES (" +
                "   :NAME" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE DOCKER.BAZ_ORACLE SET " +
                "   NAME = :NAME " +
                "WHERE ID = :ID";
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
