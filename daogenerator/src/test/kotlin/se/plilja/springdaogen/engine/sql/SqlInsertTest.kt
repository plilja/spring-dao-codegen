package se.plilja.springdaogen.engine.sql

import org.junit.jupiter.api.Test
import se.plilja.springdaogen.config.defaultTestConfig
import se.plilja.springdaogen.engine.model.Column
import se.plilja.springdaogen.engine.model.DatabaseDialect
import se.plilja.springdaogen.engine.model.Schema
import se.plilja.springdaogen.engine.model.Table
import kotlin.test.assertEquals


class SqlInsertTest {
    @Test
    fun `base case`() {
        val config = defaultTestConfig()
        val pk = Column("FOO_ID", Integer::class.java, config, true)
        val name = Column("NAME", String::class.java, config)
        val age = Column("AGE", Integer::class.java, config)
        val schema = Schema("public", ArrayList(), ArrayList())
        val table = Table(schema, "FOO", pk, listOf(pk, name, age), defaultTestConfig())
        schema.tables.add(table)

        // when
        val sql = insert(table, DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals(
                """
            |"INSERT INTO public.FOO (" +
            |"NAME, " +
            |"AGE" +
            |") " +
            |"VALUES (" +
            |":NAME, " +
            |":AGE" +
            |")"
        """.trimMargin(), sql
        )
    }

    @Test
    fun `only one generated column Postgres`() {
        val config = defaultTestConfig()
        val pk = Column("foo_id", Integer::class.java, config, true)
        val schema = Schema("public", ArrayList(), ArrayList())
        val table = Table(schema, "foo", pk, listOf(pk), defaultTestConfig())
        schema.tables.add(table)

        // when
        val sql = insert(table, DatabaseDialect.POSTGRES)

        // then
        assertEquals("\"INSERT INTO public.foo DEFAULT VALUES\"", sql)
    }

    @Test
    fun `only one generated column MS Sql`() {
        val config = defaultTestConfig()
        val pk = Column("foo_id", Integer::class.java, config, true)
        val schema = Schema("public", ArrayList(), ArrayList())
        val table = Table(schema, "foo", pk, listOf(pk), defaultTestConfig())
        schema.tables.add(table)

        // when
        val sql = insert(table, DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals("\"INSERT INTO public.foo DEFAULT VALUES\"", sql)
    }

    @Test
    fun `only one generated column Mysql`() {
        val config = defaultTestConfig()
        val pk = Column("foo_id", Integer::class.java, config, true)
        val schema = Schema("public", ArrayList(), ArrayList())
        val table = Table(schema, "foo", pk, listOf(pk), defaultTestConfig())
        schema.tables.add(table)

        // when
        val sql = insert(table, DatabaseDialect.MYSQL)

        // then
        assertEquals("\"INSERT INTO public.foo() VALUES()\"", sql)
    }

    @Test
    fun `only one generated column Oracle`() {
        val config = defaultTestConfig()
        val pk = Column("foo_id", Integer::class.java, config, true)
        val schema = Schema("public", ArrayList(), ArrayList())
        val table = Table(schema, "foo", pk, listOf(pk), defaultTestConfig())
        schema.tables.add(table)

        // when
        val sql = insert(table, DatabaseDialect.ORACLE)

        // then
        assertEquals("\"INSERT INTO \\\"public\\\".foo(foo_id) VALUES(null)\"", sql)
    }

    @Test
    fun `only one non generated column`() {
        val config = defaultTestConfig()
        val pk = Column("foo_id", Integer::class.java, config, false)
        val schema = Schema("public", ArrayList(), ArrayList())
        val table = Table(schema, "foo", pk, listOf(pk), defaultTestConfig())
        schema.tables.add(table)

        // when
        val sql = insert(table, DatabaseDialect.ORACLE)

        // then
        assertEquals(
                """
            |"INSERT INTO \"public\".foo (" +
            |"foo_id" +
            |") " +
            |"VALUES (" +
            |":foo_id" +
            |")"
        """.trimMargin(), sql
        )
    }
}