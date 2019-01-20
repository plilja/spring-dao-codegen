package se.plilja.springdaogen.sqlgeneration

import org.junit.Assert.assertEquals
import org.junit.Test
import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.DatabaseDialect
import se.plilja.springdaogen.model.Table

class SqlTest {
    @Test
    fun testInsert() {
        val pk = Column("FOO_ID", Integer::class.java)
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
    fun testUpdate() {
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)
        val age = Column("AGE", Integer::class.java)

        // when
        val sql = update(Table("public", "FOO", pk, listOf(pk, name, age)), DatabaseDialect.MSSQL_SERVER)

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
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)

        // when
        val sql = selectOne(Table("public", "FOO", pk, listOf(pk, name)), DatabaseDialect.MSSQL_SERVER)

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
    fun testSelectNoSchema() {
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)

        // when
        val sql = selectOne(Table(null, "FOO", pk, listOf(pk, name)), DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals(
            """
            |"SELECT " +
            |"   FOO_ID, " +
            |"   NAME " +
            |"FROM FOO " +
            |"WHERE FOO_ID = :FOO_ID"
        """.trimMargin(), sql
        )
    }

    @Test
    fun testExistsById() {
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)

        // when
        val sql = existsById(Table(null, "FOO", pk, listOf(pk, name)), DatabaseDialect.MSSQL_SERVER)

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
    fun testSelectManyMysql() {
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)

        // when
        val sql = selectMany(Table("public", "FOO", pk, listOf(pk, name)), DatabaseDialect.MYSQL)

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
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)

        // when
        val sql = selectMany(Table("public", "FOO", pk, listOf(pk, name)), DatabaseDialect.POSTGRES)

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
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)

        // when
        val sql = selectMany(Table("public", "FOO", pk, listOf(pk, name)), DatabaseDialect.ORACLE)

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
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)

        // when
        val sql = selectMany(Table("public", "FOO", pk, listOf(pk, name)), DatabaseDialect.MSSQL_SERVER)

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