package se.plilja.springdaogen.engine.sql

import org.junit.jupiter.api.Test
import se.plilja.springdaogen.config.defaultTestConfig
import se.plilja.springdaogen.engine.model.Column
import se.plilja.springdaogen.engine.model.DatabaseDialect
import se.plilja.springdaogen.engine.model.Schema
import se.plilja.springdaogen.engine.model.Table
import kotlin.test.assertEquals


class ColumnListTest {
    @Test
    fun `list columns table with few columns`() {
        val config = defaultTestConfig()
        val pk = Column("FOO_ID", Integer::class.java, config, true)
        val name = Column("NAME", String::class.java, config)
        val age = Column("AGE", Integer::class.java, config)
        val schema = Schema("public", ArrayList(), ArrayList())
        val table = Table(schema, "FOO", pk, listOf(pk, name, age), defaultTestConfig())
        schema.tables.add(table)

        // when
        val res = columnsList(table, DatabaseDialect.MSSQL_SERVER)

        // then
        assertEquals("\" FOO_ID, NAME, AGE \"", res)
    }

    @Test
    fun `list columns table with many columns`() {
        val config = defaultTestConfig()
        val pk = Column("id", Integer::class.java, config, true)
        val a = Column("a", Integer::class.java, config)
        val b = Column("b", Integer::class.java, config)
        val c = Column("c", Integer::class.java, config)
        val d = Column("d", Integer::class.java, config)
        val e = Column("e", Integer::class.java, config)
        val f = Column("f", Integer::class.java, config)
        val g = Column("g", Integer::class.java, config)
        val h = Column("h", Integer::class.java, config)
        val i = Column("i", Integer::class.java, config)
        val j = Column("j", Integer::class.java, config)
        val k = Column("k", Integer::class.java, config)
        val l = Column("l", Integer::class.java, config)
        val schema = Schema("public", ArrayList(), ArrayList())
        val table = Table(schema, "FOO", pk, listOf(pk, a, b, c, d, e, f, g, h, i, j, k, l), defaultTestConfig())
        schema.tables.add(table)

        // when
        val res = columnsList(
                table,
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
