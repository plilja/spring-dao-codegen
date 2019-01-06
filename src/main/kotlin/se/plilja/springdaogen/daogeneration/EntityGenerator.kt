package se.plilja.springdaogen.daogeneration

import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Table
import se.plilja.springdaogen.codegeneration.ClassGenerator


fun generateEntity(config: Config, table: Table) : ClassGenerator {
    val g = ClassGenerator(table.entityName(), config.outputPackage)
    g.implements = "BaseEntity<Integer>" // TODO resolve from PK
    for (column in table.columns) {
        g.addField(column.fieldName(), column.javaType)
    }
    g.addCustomMethod("""
    public Integer getId() {
        return ${table.primaryKey.fieldName()};
    }
    """.trimMargin())
    g.addCustomMethod("""
    public void setId(Integer id) {
        ${table.primaryKey.setter()}(id);
    }
    """.trimMargin())
    return g
}
