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
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)
        val age = Column("AGE", Integer::class.java)

        // when
        val sql = insert(Table("FOO", pk, listOf(pk, name, age)))

        // then
        assertEquals(
            """
            |"INSERT INTO FOO (" +
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
        val sql = update(Table("FOO", pk, listOf(pk, name, age)))

        // then
        assertEquals(
            """
            |"UPDATE FOO SET " +
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
        val sql = selectOne(Table("FOO", pk, listOf(pk, name)))

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
    fun testSelectManyMysqlAndPostgres() {
        for (dialect in listOf(DatabaseDialect.MYSQL, DatabaseDialect.POSTGRES)) {
            val config = Config(dialect, "", "", "", "", "", "", "", "", "", "", 0)
            val pk = Column("FOO_ID", Integer::class.java)
            val name = Column("NAME", String::class.java)

            // when
            val sql = selectMany(Table("FOO", pk, listOf(pk, name)), config)

            // then
            assertEquals(
                dialect.toString(),
                """
            |"SELECT " +
            |"   FOO_ID, " +
            |"   NAME " +
            |"FROM FOO " +
            |"LIMIT %d"
        """.trimMargin(), sql
            )
        }
    }

    @Test
    fun testSelectManyOracle() {
        val config = Config(DatabaseDialect.ORACLE, "", "", "", "", "", "", "", "", "", "",  0)
        val pk = Column("FOO_ID", Integer::class.java)
        val name = Column("NAME", String::class.java)

        // when
        val sql = selectMany(Table("FOO", pk, listOf(pk, name)), config)

        // then
        assertEquals(
            """
            |"SELECT " +
            |"   FOO_ID, " +
            |"   NAME " +
            |"FROM FOO " +
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
        val sql = selectMany(Table("FOO", pk, listOf(pk, name)), config)

        // then
        assertEquals(
            """
            |"SELECT TOP %d " +
            |"   FOO_ID, " +
            |"   NAME " +
            |"FROM FOO "
        """.trimMargin(), sql
        )
    }
}