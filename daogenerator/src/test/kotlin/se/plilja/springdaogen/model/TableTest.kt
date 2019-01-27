package se.plilja.springdaogen.model

import org.junit.Test
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

        assertEquals("Table", table.repositoryName())
        assertEquals("Table", table.copy(name = "TABLE").repositoryName())
        assertEquals("Table", table.copy(name = "table").repositoryName())
        assertEquals("TableSuffix", table.copy(repositorySuffix = "Suffix").repositoryName())
        assertEquals("PrefixTable", table.copy(repositoryPrefix = "Prefix").repositoryName())
        assertEquals("PrefixTable", table.copy(name = "table", repositoryPrefix = "Prefix").repositoryName())
        assertEquals(
            "PrefixTableSuffix",
            table.copy(repositoryPrefix = "Prefix", repositorySuffix = "Suffix").repositoryName()
        )
    }
}