package se.plilja.springdaogen.sqlgeneration

import org.junit.jupiter.api.Test
import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.DatabaseDialect
import se.plilja.springdaogen.model.Table
import kotlin.test.assertEquals


class SqlInsertTest {
    @Test
    fun `base case`() {
        val pk = Column("FOO_ID", Integer::class.java, true)
        val name = Column("NAME", String::class.java)
        val age = Column("AGE", Integer::class.java)

        // when
        val sql = insert(Table("public", "FOO", pk, listOf(pk, name, age)), DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals(
            """
            |"INSERT INTO public.FOO (" +
            |"   NAME, " +
            |"   AGE" +
            |") " +
            |"VALUES (" +
            |"   :NAME, " +
            |"   :AGE" +
            |")"
        """.trimMargin(), sql
        )
    }

    @Test
    fun `only one generated column Postgres`() {
        val pk = Column("foo_id", Integer::class.java, true)

        // when
        val sql = insert(Table("public", "foo", pk, listOf(pk)), DatabaseDialect.POSTGRES)

        // then
        assertEquals("\"INSERT INTO public.foo DEFAULT VALUES\"", sql)
    }

    @Test
    fun `only one generated column MS Sql`() {
        val pk = Column("foo_id", Integer::class.java, true)

        // when
        val sql = insert(Table("public", "foo", pk, listOf(pk)), DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals("\"INSERT INTO public.foo DEFAULT VALUES\"", sql)
    }

    @Test
    fun `only one generated column Mysql`() {
        val pk = Column("foo_id", Integer::class.java, true)

        // when
        val sql = insert(Table("public", "foo", pk, listOf(pk)), DatabaseDialect.MYSQL)

        // then
        assertEquals("\"INSERT INTO public.foo() VALUES()\"", sql)
    }

    @Test
    fun `only one generated column Oracle`() {
        val pk = Column("foo_id", Integer::class.java, true)

        // when
        val sql = insert(Table("public", "foo", pk, listOf(pk)), DatabaseDialect.ORACLE)

        // then
        assertEquals("\"INSERT INTO \\\"public\\\".foo(foo_id) VALUES(null)\"", sql)
    }

    @Test
    fun `only one non generated column`() {
        val pk = Column("foo_id", Integer::class.java, false)

        // when
        val sql = insert(Table("public", "foo", pk, listOf(pk)), DatabaseDialect.ORACLE)

        // then
        assertEquals(
            """
            |"INSERT INTO \"public\".foo (" +
            |"   foo_id" +
            |") " +
            |"VALUES (" +
            |"   :foo_id" +
            |")"
        """.trimMargin(), sql
        )
    }
}