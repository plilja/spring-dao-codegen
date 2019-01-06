package se.plilja.springdaogen

import java.io.File


fun generateConstants(schema: Schema) {
    for (table in schema.tables) {
        generateConstantsForTable(table)
    }
}

fun generateConstantsForTable(table: Table) {
    val g = ClassGenerator(table.constantsName(), "generated")
    for (column in table.columns) {
        g.addConstant(column.constantsName(), String::class.java, "\"${column.name}\"")
    }
    g.isFinal = true
    g.isConstantsClass = true
    File(Config.outputFolder()).mkdirs()
    File(Config.outputFolder() + g.name + ".java").writeText(g.generate())

}
