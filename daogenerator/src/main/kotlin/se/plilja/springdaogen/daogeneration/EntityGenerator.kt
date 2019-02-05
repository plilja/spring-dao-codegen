package se.plilja.springdaogen.daogeneration

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import se.plilja.springdaogen.codegeneration.ClassGenerator
import se.plilja.springdaogen.generatedframework.baseEntity
import se.plilja.springdaogen.generatedframework.changedAtTracked
import se.plilja.springdaogen.generatedframework.createdAtTracked
import se.plilja.springdaogen.generatedframework.versionTracked
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Table
import java.time.LocalDate


fun generateEntity(config: Config, table: Table): ClassGenerator {
    val g = ClassGenerator(table.entityName(), config.entityOutputPackage, config.entityOutputFolder)
    g.addImplements("BaseEntity<${table.primaryKey.type().simpleName}>")
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
            g.addPrivateField(column.fieldName(), column.type())
        } else if (column == table.primaryKey && column.name.toLowerCase() == "id") {
            g.addPrivateField(column.fieldName(), column.type())
        } else if (column == table.createdAtColumn() && column.getter() == "getCreatedAt") {
            g.addPrivateField(column.fieldName(), column.type())
        } else if (column == table.changedAtColumn() && column.getter() == "getChangedAt") {
            g.addPrivateField(column.fieldName(), column.type())
        } else if (column == table.versionColumn() && column.getter() == "getVersion") {
            // Always use Integer for version column no matter what the db says
            g.addPrivateField(column.fieldName(), column.type())
        } else {
            g.addField(column.fieldName(), column.type())
        }
    }
    val idMethodGeneratedByLombok = config.useLombok && table.primaryKey.name == "id"
    if (!idMethodGeneratedByLombok) {
        g.addCustomMethod(
            """
        |   @Override
        |   public ${table.primaryKey.type().simpleName} getId() {
        |       return ${table.primaryKey.fieldName()};
        |   }
    """.trimMargin()
        )
        g.addCustomMethod(
            """
        |   @Override
        |   public void setId(${table.primaryKey.type().simpleName} id) {
        |       this.${table.primaryKey.fieldName()} = id;
        |   }
        """.trimMargin()
        )
    }

    val createdAtColumn = table.createdAtColumn()
    if (createdAtColumn != null) {
        ensureImported(g, config) { createdAtTracked(config.frameworkOutputPackage) }
        g.addImplements("CreatedAtTracked<${createdAtColumn.type().simpleName}>")
        if (!config.useLombok || createdAtColumn.getter() != "getCreatedAt") {
            g.addCustomMethod(
                """
            |   @Override
            |   public ${createdAtColumn.type().simpleName} getCreatedAt() {
            |       return ${createdAtColumn.fieldName()};
            |   }
            """.trimMargin()
            )
        }
        if (!config.useLombok) {
            g.addCustomMethod(
                """
            |   public void ${createdAtColumn.setter()}(${createdAtColumn.type().simpleName} value) {
            |       this.${createdAtColumn.fieldName()} = value;
            |   }
            """.trimMargin()
            )
        }

        g.addCustomMethod(
            """
        |   @Override
        |   public void setCreatedNow() {
        |       ${createdAtColumn.fieldName()} = ${if (createdAtColumn.type() == LocalDate::class.java) "LocalDate.now()" else "LocalDateTime.now()"};
        |   }
        """.trimMargin()
        )
    }

    val changedAtColumn = table.changedAtColumn()
    if (changedAtColumn != null) {
        ensureImported(g, config) { changedAtTracked(config.frameworkOutputPackage) }
        g.addImplements("ChangedAtTracked<${changedAtColumn.type().simpleName}>")
        if (!config.useLombok || changedAtColumn.getter() != "getChangedAt") {
            g.addCustomMethod(
                """
            |   @Override
            |   public ${changedAtColumn.type().simpleName} getChangedAt() {
            |       return ${changedAtColumn.fieldName()};
            |   }
            """.trimMargin()
            )
        }
        if (!config.useLombok) {
            g.addCustomMethod(
                """
            |   public void ${changedAtColumn.setter()}(${changedAtColumn.type().simpleName} value) {
            |       this.${changedAtColumn.fieldName()} = value;
            |   }
            """.trimMargin()
            )
        }

        g.addCustomMethod(
            """
        |   @Override
        |   public void setChangedNow() {
        |       ${changedAtColumn.fieldName()} = ${if (changedAtColumn.type() == LocalDate::class.java) "LocalDate.now()" else "LocalDateTime.now()"};
        |   }
        """.trimMargin()
        )
    }

    val versionColumn = table.versionColumn()
    if (versionColumn != null) {
        ensureImported(g, config) { versionTracked(config.frameworkOutputPackage) }
        g.addImplements("VersionTracked")
        if (!config.useLombok || versionColumn.getter() != "getVersion") {
            g.addCustomMethod(
                """
            |   @Override
            |   public ${versionColumn.type().simpleName} getVersion() {
            |       return ${versionColumn.fieldName()};
            |   }
            """.trimMargin()
            )


            g.addCustomMethod(
                """
            |   @Override
            |   public void setVersion(${versionColumn.type().simpleName} value) {
            |       this.${versionColumn.fieldName()} = value;
            |   }
            """.trimMargin()
            )
        }
    }

    ensureImported(g, config) { baseEntity(config.frameworkOutputPackage) }

    return g
}

fun ensureImported(g: ClassGenerator, config: Config, f: () -> Pair<String, String>) {
    if (config.frameworkOutputPackage != config.entityOutputPackage) {
        g.addImport("${config.frameworkOutputPackage}.${f().first}")
    }
}
