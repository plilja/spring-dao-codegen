package se.plilja.springdaogen


fun generateConstants(table: Table): ClassGenerator {
    val g = ClassGenerator(table.constantsName(), "generated")
    for (column in table.columns) {
        g.addConstant(column.constantsName(), String::class.java, "\"${column.name}\"")
    }
    g.isFinal = true
    g.isConstantsClass = true
    return g
}
