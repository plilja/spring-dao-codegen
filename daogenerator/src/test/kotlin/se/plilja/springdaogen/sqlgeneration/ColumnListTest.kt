package se.plilja.springdaogen.sqlgeneration

import org.junit.jupiter.api.Test
import se.plilja.springdaogen.defaultTestConfig
import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.DatabaseDialect
import se.plilja.springdaogen.model.Table
import kotlin.test.assertEquals


class ColumnListTest {
    @Test
    fun `list columns table with few columns`() {
        val pk = Column("FOO_ID", Integer::class.java, true)
        val name = Column("NAME", String::class.java)
        val age = Column("AGE", Integer::class.java)

        // when
        val res = columnsList(Table("public", "FOO", pk, listOf(pk, name, age), defaultTestConfig()), DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals("\" FOO_ID, NAME, AGE \"", res)
    }

    @Test
    fun `list columns table with many columns`() {
        val pk = Column("id", Integer::class.java, true)
        val a = Column("a", Integer::class.java)
        val b = Column("b", Integer::class.java)
        val c = Column("c", Integer::class.java)
        val d = Column("d", Integer::class.java)
        val e = Column("e", Integer::class.java)
        val f = Column("f", Integer::class.java)
        val g = Column("g", Integer::class.java)
        val h = Column("h", Integer::class.java)
        val i = Column("i", Integer::class.java)
        val j = Column("j", Integer::class.java)
        val k = Column("k", Integer::class.java)
        val l = Column("l", Integer::class.java)

        // when
        val res = columnsList(
            Table("public", "FOO", pk, listOf(pk, a, b, c, d, e, f, g, h, i, j, k, l), defaultTestConfig()),
            DatabaseDialect.MYSQL
        )

        // then
        val exp = """
            " id, a, b, c, d, " +
            " e, f, g, h, i, " +
            " j, k, l "
        """.trimIndent()
        assertEquals(exp, res)
    }
}
