package se.plilja.springdaogen.sqlgeneration

import se.plilja.springdaogen.model.DatabaseDialect


class SqlKeywords {
    companion object {
        private val cache = HashMap<DatabaseDialect, List<String>>()

        fun get(databaseDialect: DatabaseDialect): List<String> {
            return when (databaseDialect) {
                DatabaseDialect.ORACLE -> cache.getOrPut(DatabaseDialect.ORACLE) { readKeyWords("/oracle_keywords.txt") }
                DatabaseDialect.ORACLE12 -> cache.getOrPut(DatabaseDialect.ORACLE12) { readKeyWords("/oracle_keywords.txt") }
                DatabaseDialect.MYSQL -> cache.getOrPut(DatabaseDialect.MYSQL) { readKeyWords("/mysql_keywords.txt") }
                else -> emptyList() // TODO
            }
        }

        private fun readKeyWords(fileName: String): List<String> {
            return SqlKeywords::class.java.getResource(fileName).readText().lines()
                .map { it.trim() }
                .map { it.toUpperCase() }
        }
    }
}
