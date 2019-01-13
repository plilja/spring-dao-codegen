package se.plilja.springdaogen.daogeneration

import org.junit.Assert.assertEquals
import org.junit.Test
import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.DatabaseDialect
import se.plilja.springdaogen.model.Table

class RepositoryGeneratorTest {

    @Test
    fun tableWithOnlyOneColumn() {
        // TODO
    }

    @Test
    fun tableWithIdColumnNamedID() {
        val config = Config(
            DatabaseDialect.MYSQL,
            "",
            "",
            "",
            "",
            "",
            "se.plilja.test",
            "",
            "se.plilja.test",
            "",
            "se.plilja.test",
            10
        )
        val pk = Column("ID", Integer::class.java)
        val name = Column("name", String::class.java)
        val table = Table("dbo", "Table", pk, listOf(pk, name))

        // when
        val res = generateRepository(config, table)
        val act = res.generate()

        println(act)

        // then
        val exp = """
package se.plilja.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class TableRepository extends BaseRepository<TableEntity, Integer> {

    private static final RowMapper<TableEntity> ROW_MAPPER = (rs, i) -> {
        return new TableEntity()
                .setId(rs.getObject("ID") != null ? rs.getInt("ID") : null)
                .setName(rs.getString("name"));
    };

    @Autowired
    public TableRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, jdbcTemplate, ROW_MAPPER);
    }

    @Override
    public SqlParameterSource getParams(TableEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("ID", o.getId());
        m.addValue("name", o.getName());
        return m;
    }

    @Override
    protected String getSelectOneSql() {
        return "SELECT " +
                "   ID, " +
                "   name " +
                "FROM dbo.Table " +
                "WHERE ID = :ID";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                "   ID, " +
                "   name " +
                "FROM dbo.Table " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO dbo.Table (" +
                "   name" +
                ") " +
                "VALUES (" +
                "   :name" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE dbo.Table SET " +
                "   name = :name " +
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

        """.trimIndent()
        assertEquals(exp, act)
    }

    @Test
    fun tableGenerateTable() {
        val config = Config(
            DatabaseDialect.MYSQL,
            "",
            "",
            "",
            "",
            "",
            "se.plilja.test",
            "",
            "se.plilja.test",
            "",
            "se.plilja.test",
            10
        )
        val pk = Column("tableId", Integer::class.java)
        val name = Column("name", String::class.java)
        val table = Table("dbo", "Table", pk, listOf(pk, name))

        // when
        val res = generateRepository(config, table)
        val act = res.generate()

        println(act)

        // then
        val exp = """
package se.plilja.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class TableRepository extends BaseRepository<TableEntity, Integer> {

    private static final RowMapper<TableEntity> ROW_MAPPER = (rs, i) -> {
        return new TableEntity()
                .setId(rs.getObject("tableId") != null ? rs.getInt("tableId") : null)
                .setName(rs.getString("name"));
    };

    @Autowired
    public TableRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, jdbcTemplate, ROW_MAPPER);
    }

    @Override
    public SqlParameterSource getParams(TableEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("tableId", o.getId());
        m.addValue("name", o.getName());
        return m;
    }

    @Override
    protected String getSelectOneSql() {
        return "SELECT " +
                "   tableId, " +
                "   name " +
                "FROM dbo.Table " +
                "WHERE tableId = :tableId";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                "   tableId, " +
                "   name " +
                "FROM dbo.Table " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO dbo.Table (" +
                "   name" +
                ") " +
                "VALUES (" +
                "   :name" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE dbo.Table SET " +
                "   name = :name " +
                "WHERE tableId = :tableId";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "tableId";
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}

        """.trimIndent()
        assertEquals(exp, act)
    }

}