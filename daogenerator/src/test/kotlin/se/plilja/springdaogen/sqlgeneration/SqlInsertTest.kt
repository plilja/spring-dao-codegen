package se.plilja.springdaogen.sqlgeneration

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.DatabaseDialect
import se.plilja.springdaogen.model.Table


class SqlInsertTest {
    @Test
    fun baseCase() {
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)
        val age = Column("AGE", Integer::class.java)

        // when
        val sql = insert(Table("public", "FOO", pk, listOf(pk, name, age)), DatabaseDialect.MSSQL_SERVER)

        // then
        Assert.assertEquals(
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
    fun oneColumnPostgres() {
        val pk = Column("foo_id", Integer::class.java)

        // when
        val sql = insert(Table("public", "foo", pk, listOf(pk)), DatabaseDialect.POSTGRES)

        // then
        assertEquals("\"INSERT INTO public.foo DEFAULT VALUES\"", sql)
    }

    @Test
    fun oneColumnMssql() {
        val pk = Column("foo_id", Integer::class.java)

        // when
        val sql = insert(Table("public", "foo", pk, listOf(pk)), DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals("\"INSERT INTO public.foo DEFAULT VALUES\"", sql)
    }

    @Test
    fun oneColumnMysql() {
        val pk = Column("foo_id", Integer::class.java)

        // when
        val sql = insert(Table("public", "foo", pk, listOf(pk)), DatabaseDialect.MYSQL)

        // then
        assertEquals("\"INSERT INTO public.foo() VALUES()\"", sql)
    }

    @Test
    fun oneColumnOracle() {
        val pk = Column("foo_id", Integer::class.java)

        // when
        val sql = insert(Table("public", "foo", pk, listOf(pk)), DatabaseDialect.ORACLE)

        // then
        assertEquals("\"INSERT INTO public.foo(foo_id) VALUES(null)\"", sql)
    }
}