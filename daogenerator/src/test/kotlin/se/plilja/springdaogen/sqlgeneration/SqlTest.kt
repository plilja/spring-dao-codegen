package se.plilja.springdaogen.sqlgeneration

import org.junit.jupiter.api.Test
import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.DatabaseDialect
import se.plilja.springdaogen.model.Table
import kotlin.test.assertEquals

class SqlTest {

    @Test
    fun update() {
        val pk = Column("FOO_ID", Integer::class.java, true)
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
    fun `select one`() {
        val pk = Column("FOO_ID", Integer::class.java, true)
        val name = Column("NAME", String::class.java)

        // when
        val sql = selectOne(Table("public", "FOO", pk, listOf(pk, name)), DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals(
            """
            |"SELECT " +
            |"FOO_ID, " +
            |"NAME " +
            |"FROM public.FOO " +
            |"WHERE FOO_ID IN (:ids)"
        """.trimMargin(), sql
        )
    }

    @Test
    fun `select no schema`() {
        val pk = Column("FOO_ID", Integer::class.java, true)
        val name = Column("NAME", String::class.java)

        // when
        val sql = selectOne(Table(null, "FOO", pk, listOf(pk, name)), DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals(
            """
            |"SELECT " +
            |"FOO_ID, " +
            |"NAME " +
            |"FROM FOO " +
            |"WHERE FOO_ID IN (:ids)"
        """.trimMargin(), sql
        )
    }

    @Test
    fun existsById() {
        val pk = Column("FOO_ID", Integer::class.java, true)
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
    fun delete() {
        val pk = Column("FOO_ID", Integer::class.java, true)
        val name = Column("NAME", String::class.java)

        // when
        val sql = delete(Table(null, "FOO", pk, listOf(pk, name)), DatabaseDialect.MSSQL_SERVER)

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
        val pk = Column("FOO_ID", Integer::class.java, true)
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
    fun `select many Postgres`() {
        val pk = Column("FOO_ID", Integer::class.java, true)
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
    fun `select many Oracle`() {
        val pk = Column("FOO_ID", Integer::class.java, true)
        val name = Column("NAME", String::class.java)

        // when
        val sql = selectMany(Table("public", "FOO", pk, listOf(pk, name)), DatabaseDialect.ORACLE)

        // then
        assertEquals(
            """
            |"SELECT " +
            |"   FOO_ID, " +
            |"   NAME " +
            |"FROM \"public\".FOO " +
            |"WHERE ROWNUM <= %d"
        """.trimMargin(), sql
        )
    }

    @Test
    fun `select many MS Sql`() {
        val pk = Column("FOO_ID", Integer::class.java, true)
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

    @Test
    fun count() {
        val pk = Column("FOO_ID", Integer::class.java, true)
        val name = Column("NAME", String::class.java)

        // when
        val sql = count(Table(null, "FOO", pk, listOf(pk, name)), DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals("\"SELECT COUNT(*) FROM FOO\"", sql)
    }
}