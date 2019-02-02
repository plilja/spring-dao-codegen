package se.plilja.springdaogen.model

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TableTest {
    @Test
    fun getEntityName() {
        val pk = Column("id", Int::class.java, true)
        val table = Table("entityfoo", "Table", pk, listOf(pk))

        assertEquals("Table", table.entityName())
        assertEquals("Table", table.copy(name = "TABLE").entityName())
        assertEquals("Table", table.copy(name = "table").entityName())
        assertEquals("TableSuffix", table.copy(entitySuffix = "Suffix").entityName())
        assertEquals("PrefixTable", table.copy(entityPrefix = "Prefix").entityName())
        assertEquals("PrefixTable", table.copy(name = "table", entityPrefix = "Prefix").entityName())
        assertEquals("PrefixTableSuffix", table.copy(entityPrefix = "Prefix", entitySuffix = "Suffix").entityName())
    }

    @Test
    fun getRepositoryName() {
        val pk = Column("id", Int::class.java, true)
        val table = Table("entityfoo", "Table", pk, listOf(pk))

        assertEquals("Table", table.daoName())
        assertEquals("Table", table.copy(name = "TABLE").daoName())
        assertEquals("Table", table.copy(name = "table").daoName())
        assertEquals("TableSuffix", table.copy(daoSuffix = "Suffix").daoName())
        assertEquals("PrefixTable", table.copy(daoPrefix = "Prefix").daoName())
        assertEquals("PrefixTable", table.copy(name = "table", daoPrefix = "Prefix").daoName())
        assertEquals(
            "PrefixTableSuffix",
            table.copy(daoPrefix = "Prefix", daoSuffix = "Suffix").daoName()
        )
    }
}