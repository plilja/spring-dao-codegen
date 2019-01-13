package se.plilja.springdaogen.daogeneration

import se.plilja.springdaogen.codegeneration.ClassGenerator
import se.plilja.springdaogen.generatedframework.baseEntity
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Table


fun generateEntity(config: Config, table: Table) : ClassGenerator {
    val g = ClassGenerator(table.entityName(), config.entityOutputPackage, config.entityOutputFolder)
    g.implements = "BaseEntity<${table.entityName()}, ${table.primaryKey.javaType.simpleName}>"
    g.addPrivateField(table.primaryKey.fieldName(), table.primaryKey.javaType)
    for (column in table.columns) {
        if (column != table.primaryKey) {
            g.addField(column.fieldName(), column.javaType)
        }
    }
    g.addCustomMethod("""
    public ${table.primaryKey.javaType.simpleName} getId() {
        return ${table.primaryKey.fieldName()};
    }
    """)
    g.addCustomMethod("""
    public ${table.entityName()} setId(${table.primaryKey.javaType.simpleName} id) {
        this.${table.primaryKey.fieldName()} = id;
        return this;
    }
    """)
    if (config.frameworkOutputPackage != config.entityOutputPackage) {
        g.addImport("${config.frameworkOutputPackage}.${baseEntity(config.frameworkOutputPackage).first}")
    }

    return g
}
