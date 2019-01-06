package se.plilja.springdaogen


fun generateConstants(config: Config,  table: Table): ClassGenerator {
    val g = ClassGenerator(table.constantsName(), config.outputPackage)
    for (column in table.columns) {
        g.addConstant(column.constantsName(), String::class.java, "\"${column.name}\"")
    }
    g.isFinal = true
    g.isConstantsClass = true
    return g
}
