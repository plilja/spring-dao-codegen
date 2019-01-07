package se.plilja.springdaogen.daogeneration

import se.plilja.springdaogen.codegeneration.ClassGenerator
import se.plilja.springdaogen.generatedframework.baseEntity
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Table


fun generateEntity(config: Config, table: Table) : ClassGenerator {
    val g = ClassGenerator(table.entityName(), config.entityOutputPackage, config.entityOutputFolder)
    g.implements = "BaseEntity<${table.primaryKey.javaType.simpleName}>"
    for (column in table.columns) {
        g.addField(column.fieldName(), column.javaType)
    }
    g.addCustomMethod("""
    public ${table.primaryKey.javaType.simpleName} getId() {
        return ${table.primaryKey.fieldName()};
    }
    """)
    g.addCustomMethod("""
    public void setId(${table.primaryKey.javaType.simpleName} id) {
        ${table.primaryKey.setter()}(id);
    }
    """)
    if (config.frameworkOutputPackage != config.entityOutputPackage) {
        g.addImport("${config.frameworkOutputPackage}.${baseEntity(config.frameworkOutputPackage).first}")
    }

    return g
}
