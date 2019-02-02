package se.plilja.springdaogen.daogeneration

import org.junit.jupiter.api.Test
import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.DatabaseDialect
import se.plilja.springdaogen.model.Table
import kotlin.test.assertEquals

class DaoGeneratorTest {

    @Test
    fun `table with only one column`() {
        // TODO
    }

    @Test
    fun `table with aggregate id columns`() {
        // TODO
    }

    @Test
    fun `table with column named "id"`() {
        val config = Config(
            "",
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
            10,
            emptyList(),
            false
        )
        val pk = Column("ID", Integer::class.java, true)
        val name = Column("name", String::class.java)
        val table =
            Table("dbo", "Table", pk, listOf(pk, name), daoSuffix = "Repository", entitySuffix = "Entity")

        // when
        val res = generateDao(config, table)
        val act = res.generate()

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
public class TableRepository extends Dao<TableEntity, Integer> {

    private static final RowMapper<TableEntity> ROW_MAPPER = (rs, i) -> {
        TableEntity r = new TableEntity();
        r.setId(rs.getObject("ID") != null ? rs.getInt("ID") : null);
        r.setName(rs.getString("name"));
        return r;
    };

    @Autowired
    public TableRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(TableEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("ID", o.getId());
        m.addValue("name", o.getName());
        return m;
    }

    @Override
    protected RowMapper<TableEntity> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM dbo.Table " +
                "WHERE ID = :ID";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                "ID, " +
                "name " +
                "FROM dbo.Table " +
                "WHERE ID IN (:ids)";
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
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                "ID, %n" +
                "name %n" +
                "FROM dbo.Table %n" +
                "LIMIT %d OFFSET %d", pageSize, start);
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
    protected String getDeleteSql() {
        return "DELETE FROM dbo.Table " +
                "WHERE ID IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM dbo.Table";
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
    fun `generate table`() {
        val config = Config(
            "",
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
            10,
            emptyList(),
            false
        )
        val pk = Column("tableId", Integer::class.java, true)
        val name = Column("name", String::class.java)
        val table =
            Table("dbo", "Table", pk, listOf(pk, name), daoSuffix = "Repository", entitySuffix = "Entity")

        // when
        val res = generateDao(config, table)
        val act = res.generate()

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
public class TableRepository extends Dao<TableEntity, Integer> {

    private static final RowMapper<TableEntity> ROW_MAPPER = (rs, i) -> {
        TableEntity r = new TableEntity();
        r.setTableId(rs.getObject("tableId") != null ? rs.getInt("tableId") : null);
        r.setName(rs.getString("name"));
        return r;
    };

    @Autowired
    public TableRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(TableEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("tableId", o.getId());
        m.addValue("name", o.getName());
        return m;
    }

    @Override
    protected RowMapper<TableEntity> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM dbo.Table " +
                "WHERE tableId = :tableId";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                "tableId, " +
                "name " +
                "FROM dbo.Table " +
                "WHERE tableId IN (:ids)";
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
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                "tableId, %n" +
                "name %n" +
                "FROM dbo.Table %n" +
                "LIMIT %d OFFSET %d", pageSize, start);
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
    protected String getDeleteSql() {
        return "DELETE FROM dbo.Table " +
                "WHERE tableId IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM dbo.Table";
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


    @Test
    fun `abstract repository`() {
        val config = Config(
            "",
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
            10,
            emptyList(),
            false,
            daosAreAbstract = true,
            daoPrefix = "Abstract"
        )
        val pk = Column("id", Integer::class.java, true)
        val name = Column("name", String::class.java)
        val table =
            Table("dbo", "Table", pk, listOf(pk, name), daoPrefix = "Abstract", daoSuffix = "Repository")

        // when
        val res = generateDao(config, table)
        val act = res.generate()

        // then
        val exp = """
package se.plilja.test;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public abstract class AbstractTableRepository extends Dao<Table, Integer> {

    private static final RowMapper<Table> ROW_MAPPER = (rs, i) -> {
        Table r = new Table();
        r.setId(rs.getObject("id") != null ? rs.getInt("id") : null);
        r.setName(rs.getString("name"));
        return r;
    };

    public AbstractTableRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(Table o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId());
        m.addValue("name", o.getName());
        return m;
    }

    @Override
    protected RowMapper<Table> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM dbo.Table " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                "id, " +
                "name " +
                "FROM dbo.Table " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                "   id, " +
                "   name " +
                "FROM dbo.Table " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                "id, %n" +
                "name %n" +
                "FROM dbo.Table %n" +
                "LIMIT %d OFFSET %d", pageSize, start);
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
                "WHERE id = :id";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM dbo.Table " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM dbo.Table";
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

        """.trimIndent()
        assertEquals(exp, act)
    }
}