package se.plilja.springdaogen

import schemacrawler.schema.Catalog
import schemacrawler.schema.Table
import java.io.File


fun generateConstants(catalog: Catalog) {
    for (table in catalog.tables) {
        generateConstantsForTable(table)
    }
}

fun generateConstantsForTable(table: Table) {
    val g = ClassGenerator(capitalizeFirst(camelCase(table.name)) + "Db", "generated")
    for (column in table.columns) {
        g.addConstant(snakeCase(column.name).toUpperCase(), String::class.java, "\"${column.name}\"")
    }
    g.isFinal = true
    g.isConstantsClass = true
    File(Config.outputFolder()).mkdirs()
    File(Config.outputFolder() + g.name + ".java").writeText(g.generate())

}
