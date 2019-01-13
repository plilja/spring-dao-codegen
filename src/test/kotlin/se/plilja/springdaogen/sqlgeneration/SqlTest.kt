package se.plilja.springdaogen.sqlgeneration

import org.junit.Assert.assertEquals
import org.junit.Test
import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.DatabaseDialect
import se.plilja.springdaogen.model.Table

class SqlTest {
    @Test
    fun testInsert() {
        val config = Config(DatabaseDialect.MSSQL_SERVER, "", "", "", "", "", "", "", "", "", "", 0)
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)
        val age = Column("AGE", Integer::class.java)

        // when
        val sql = insert(Table("public", "FOO", pk, listOf(pk, name, age)), config)

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
    fun testUpdate() {
        val config = Config(DatabaseDialect.MSSQL_SERVER, "", "", "", "", "", "", "", "", "", "", 0)
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)
        val age = Column("AGE", Integer::class.java)

        // when
        val sql = update(Table("public", "FOO", pk, listOf(pk, name, age)), config)

        // then
        assertEquals(
            """
            |"UPDATE public.FOO SET " +
            |"   NAME = :NAME, " +
            |"   AGE = :AGE " +
            |"WHERE FOO_ID = :FOO_ID"
        """.trimMargin(), sql
        )
    }

    @Test
    fun testSelectOne() {
        val config = Config(DatabaseDialect.MSSQL_SERVER, "", "", "", "", "", "", "", "", "", "", 0)
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)

        // when
        val sql = selectOne(Table("public", "FOO", pk, listOf(pk, name)), config)

        // then
        assertEquals(
            """
            |"SELECT " +
            |"   FOO_ID, " +
            |"   NAME " +
            |"FROM public.FOO " +
            |"WHERE FOO_ID = :FOO_ID"
        """.trimMargin(), sql
        )
    }

    @Test
    fun testSelectManyMysql() {
        val config = Config(DatabaseDialect.MYSQL, "", "", "", "", "", "", "", "", "", "", 0)
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)

        // when
        val sql = selectMany(Table("public", "FOO", pk, listOf(pk, name)), config)

        // then
        assertEquals(
            """
            |"SELECT " +
            |"   FOO_ID, " +
            |"   NAME " +
            |"FROM public.FOO " +
            |"LIMIT %d"
        """.trimMargin(), sql
        )
    }

    @Test
    fun testSelectManyPostgres() {
        val config = Config(DatabaseDialect.POSTGRES, "", "", "", "", "", "", "", "", "", "", 0)
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)

        // when
        val sql = selectMany(Table("public", "FOO", pk, listOf(pk, name)), config)

        // then
        assertEquals(
            """
            |"SELECT " +
            |"   \"FOO_ID\", " +
            |"   \"NAME\" " +
            |"FROM public.\"FOO\" " +
            |"LIMIT %d"
        """.trimMargin(), sql
        )
    }

    @Test
    fun testSelectManyOracle() {
        val config = Config(DatabaseDialect.ORACLE, "", "", "", "", "", "", "", "", "", "", 0)
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)

        // when
        val sql = selectMany(Table("public", "FOO", pk, listOf(pk, name)), config)

        // then
        assertEquals(
            """
            |"SELECT " +
            |"   FOO_ID, " +
            |"   NAME " +
            |"FROM public.FOO " +
            |"WHERE ROWNUM <= %d"
        """.trimMargin(), sql
        )
    }

    @Test
    fun testSelectManyMsSqlServer() {
        val config = Config(DatabaseDialect.MSSQL_SERVER, "", "", "", "", "", "", "", "", "", "", 0)
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)

        // when
        val sql = selectMany(Table("public", "FOO", pk, listOf(pk, name)), config)

        // then
        assertEquals(
            """
            |"SELECT TOP %d " +
            |"   FOO_ID, " +
            |"   NAME " +
            |"FROM public.FOO "
        """.trimMargin(), sql
        )
    }
}