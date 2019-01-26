package se.plilja.springdaogen.daogeneration

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import se.plilja.springdaogen.codegeneration.ClassGenerator
import se.plilja.springdaogen.generatedframework.baseEntity
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Table


fun generateEntity(config: Config, table: Table): ClassGenerator {
    val g = ClassGenerator(table.entityName(), config.entityOutputPackage, config.entityOutputFolder)
    g.implements = "BaseEntity<${table.primaryKey.javaType.simpleName}>"
    if (config.useLombok) {
        g.addClassAnnotation("@Data")
        g.addClassAnnotation("@NoArgsConstructor")
        g.addClassAnnotation("@AllArgsConstructor")
        g.addImport(Data::class.java)
        g.addImport(AllArgsConstructor::class.java)
        g.addImport(NoArgsConstructor::class.java)
        g.hideConstructors = true
    }
    for (column in table.columns) {
        if (config.useLombok) {
            g.addPrivateField(column.fieldName(), column.javaType)
        } else if (column == table.primaryKey && column.name.toLowerCase() == "id") {
            g.addPrivateField(column.fieldName(), column.javaType)
        } else {
            g.addField(column.fieldName(), column.javaType)
        }
    }
    g.addCustomMethod(
        """
        |   @Override
        |   public ${table.primaryKey.javaType.simpleName} getId() {
        |       return ${table.primaryKey.fieldName()};
        |   }
    """.trimMargin()
    )
    g.addCustomMethod(
        """
        |   @Override
        |   public void setId(${table.primaryKey.javaType.simpleName} id) {
        |       this.${table.primaryKey.fieldName()} = id;
        |   }
        """.trimMargin()
    )

    if (config.frameworkOutputPackage != config.entityOutputPackage) {
        g.addImport("${config.frameworkOutputPackage}.${baseEntity(config.frameworkOutputPackage).first}")
    }

    return g
}
