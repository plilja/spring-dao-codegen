package se.plilja.springdaogen.engine.sql

import org.junit.jupiter.api.Test
import se.plilja.springdaogen.config.defaultTestConfig
import se.plilja.springdaogen.engine.model.Column
import se.plilja.springdaogen.engine.model.DatabaseDialect
import se.plilja.springdaogen.engine.model.Schema
import se.plilja.springdaogen.engine.model.Table
import kotlin.test.assertEquals

class SqlTest {

    @Test
    fun update() {
        val config = defaultTestConfig()
        val pk = Column("FOO_ID", Integer::class.java, config, true)
        val name = Column("NAME", String::class.java, config)
        val age = Column("AGE", Integer::class.java, config)
        val schema = Schema("public", ArrayList(), ArrayList())
        val table = Table(schema, "FOO", pk, listOf(pk, name, age), defaultTestConfig())
        schema.tables.add(table)

        // when
        val sql = update(table, DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals(
                """
            |"UPDATE public.FOO SET " +
            |"NAME = :NAME, " +
            |"AGE = :AGE " +
            |"WHERE FOO_ID = :FOO_ID"
        """.trimMargin(), sql
        )
    }

    @Test
    fun `select one`() {
        val config = defaultTestConfig()
        val pk = Column("FOO_ID", Integer::class.java, config, true)
        val name = Column("NAME", String::class.java, config)
        val schema = Schema("public", ArrayList(), ArrayList())
        val table = Table(schema, "FOO", pk, listOf(pk, name), defaultTestConfig())
        schema.tables.add(table)

        // when
        val sql = selectOne(table, DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals(
                """
            |"SELECT " +
            |ALL_COLUMNS +
            |"FROM public.FOO " +
            |"WHERE FOO_ID IN (:ids)"
        """.trimMargin(), sql
        )
    }

    @Test
    fun `select no schema`() {
        val config = defaultTestConfig()
        val pk = Column("FOO_ID", Integer::class.java, config, true)
        val name = Column("NAME", String::class.java, config)
        val schema = Schema(null, ArrayList(), ArrayList()) // It represents the implicit default schema
        val table = Table(schema, "FOO", pk, listOf(pk, name), defaultTestConfig())
        schema.tables.add(table)

        // when
        val sql = selectOne(table, DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals(
                """
            |"SELECT " +
            |ALL_COLUMNS +
            |"FROM FOO " +
            |"WHERE FOO_ID IN (:ids)"
        """.trimMargin(), sql
        )
    }

    @Test
    fun existsById() {
        val config = defaultTestConfig()
        val pk = Column("FOO_ID", Integer::class.java, config, true)
        val name = Column("NAME", String::class.java, config)
        val schema = Schema(null, ArrayList(), ArrayList())
        val table = Table(schema, "FOO", pk, listOf(pk, name), defaultTestConfig())
        schema.tables.add(table)

        // when
        val sql = existsById(table, DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals(
                """
            |"SELECT " +
            |"COUNT(*) " +
            |"FROM FOO " +
            |"WHERE FOO_ID = :FOO_ID"
        """.trimMargin(), sql
        )
    }

    @Test
    fun delete() {
        val config = defaultTestConfig()
        val pk = Column("FOO_ID", Integer::class.java, config, true)
        val name = Column("NAME", String::class.java, config)
        val schema = Schema(null, ArrayList(), ArrayList())
        val table = Table(schema, "FOO", pk, listOf(pk, name), defaultTestConfig())
        schema.tables.add(table)

        // when
        val sql = delete(table, DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals(
                """
            |"DELETE FROM FOO " +
            |"WHERE FOO_ID IN (:ids)"
        """.trimMargin(), sql
        )
    }

    @Test
    fun `select many Mysql`() {
        val config = defaultTestConfig()
        val pk = Column("FOO_ID", Integer::class.java, config, true)
        val name = Column("NAME", String::class.java, config)
        val schema = Schema("public", ArrayList(), ArrayList())
        val table = Table(schema, "FOO", pk, listOf(pk, name), defaultTestConfig())
        schema.tables.add(table)

        // when
        val sql = selectMany(table, DatabaseDialect.MYSQL)

        // then
        assertEquals(
                """
            |"SELECT " +
            |ALL_COLUMNS +
            |"FROM public.FOO " +
            |"LIMIT %d"
        """.trimMargin(), sql
        )
    }

    @Test
    fun `select many Postgres`() {
        val config = defaultTestConfig()
        val pk = Column("FOO_ID", Integer::class.java, config, true)
        val name = Column("NAME", String::class.java, config)
        val schema = Schema("public", ArrayList(), ArrayList())
        val table = Table(schema, "FOO", pk, listOf(pk, name), defaultTestConfig())
        schema.tables.add(table)

        // when
        val sql = selectMany(table, DatabaseDialect.POSTGRES)

        // then
        assertEquals(
                """
            |"SELECT " +
            |ALL_COLUMNS +
            |"FROM public.\"FOO\" " +
            |"LIMIT %d"
        """.trimMargin(), sql
        )
    }

    @Test
    fun `select many Oracle`() {
        val config = defaultTestConfig()
        val pk = Column("FOO_ID", Integer::class.java, config, true)
        val name = Column("NAME", String::class.java, config)
        val schema = Schema("public", ArrayList(), ArrayList())
        val table = Table(schema, "FOO", pk, listOf(pk, name), defaultTestConfig())
        schema.tables.add(table)

        // when
        val sql = selectMany(table, DatabaseDialect.ORACLE)

        // then
        assertEquals(
                """
            |"SELECT " +
            |ALL_COLUMNS +
            |"FROM \"public\".FOO " +
            |"WHERE ROWNUM <= %d"
        """.trimMargin(), sql
        )
    }

    @Test
    fun `select many MS Sql`() {
        val config = defaultTestConfig()
        val pk = Column("FOO_ID", Integer::class.java, config, true)
        val name = Column("NAME", String::class.java, config)
        val schema = Schema("public", ArrayList(), ArrayList())
        val table = Table(schema, "FOO", pk, listOf(pk, name), defaultTestConfig())
        schema.tables.add(table)

        // when
        val sql = selectMany(table, DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals(
                """
            |"SELECT TOP %d " +
            |ALL_COLUMNS +
            |"FROM public.FOO "
        """.trimMargin(), sql
        )
    }

    @Test
    fun count() {
        val config = defaultTestConfig()
        val pk = Column("FOO_ID", Integer::class.java, config, true)
        val name = Column("NAME", String::class.java, config)
        val schema = Schema(null, ArrayList(), ArrayList())
        val table = Table(schema, "FOO", pk, listOf(pk, name), defaultTestConfig())
        schema.tables.add(table)

        // when
        val sql = count(table, DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals("\"SELECT COUNT(*) FROM FOO\"", sql)
    }
}