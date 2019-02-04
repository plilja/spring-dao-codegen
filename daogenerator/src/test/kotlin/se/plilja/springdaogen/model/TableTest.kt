package se.plilja.springdaogen.model

import org.junit.jupiter.api.Test
import se.plilja.springdaogen.defaultTestConfig
import kotlin.test.assertEquals

class TableTest {
    @Test
    fun getEntityName() {
        val pk = Column("id", Int::class.java, true)
        val config = defaultTestConfig().copy(entityPrefix = "", entitySuffix = "")
        val table = Table("entityfoo", "Table", pk, listOf(pk), config)

        assertEquals("Table", table.entityName())
        assertEquals("Table", table.copy(name = "TABLE").entityName())
        assertEquals("Table", table.copy(name = "table").entityName())
        assertEquals("TableSuffix", table.copy(config = config.copy(entitySuffix = "Suffix")).entityName())
        assertEquals("PrefixTable", table.copy(config = config.copy(entityPrefix = "Prefix")).entityName())
        assertEquals("PrefixTable", table.copy(name = "table", config = config.copy(entityPrefix =  "Prefix")).entityName())
        assertEquals("PrefixTableSuffix", table.copy(config = config.copy(entityPrefix = "Prefix", entitySuffix = "Suffix")).entityName())
    }

    @Test
    fun getRepositoryName() {
        val pk = Column("id", Int::class.java, true)
        val config = defaultTestConfig().copy(daoPrefix = "", daoSuffix = "")
        val table = Table("entityfoo", "Table", pk, listOf(pk), config)

        assertEquals("Table", table.daoName())
        assertEquals("Table", table.copy(name = "TABLE").daoName())
        assertEquals("Table", table.copy(name = "table").daoName())
        assertEquals("TableSuffix", table.copy(config = config.copy(daoSuffix = "Suffix")).daoName())
        assertEquals("PrefixTable", table.copy(config = config.copy(daoPrefix = "Prefix")).daoName())
        assertEquals("PrefixTable", table.copy(name = "table", config = config.copy(daoPrefix = "Prefix")).daoName())
        assertEquals(
            "PrefixTableSuffix",
            table.copy(config = config.copy(daoPrefix = "Prefix", daoSuffix = "Suffix")).daoName()
        )
    }
}