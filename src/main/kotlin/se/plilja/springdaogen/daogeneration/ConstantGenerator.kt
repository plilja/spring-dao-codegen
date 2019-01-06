package se.plilja.springdaogen.daogeneration

import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Table
import se.plilja.springdaogen.codegeneration.ClassGenerator


// TODO consider removing. It's not used in generated SQL
fun generateConstants(config: Config, table: Table): ClassGenerator {
    val g = ClassGenerator(table.constantsName(), config.outputPackage)
    for (column in table.columns) {
        g.addConstant(column.constantsName(), String::class.java, "\"${column.name}\"")
    }
    g.isFinal = true
    g.isConstantsClass = true
    return g
}
