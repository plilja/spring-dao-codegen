package se.plilja.springdaogen.daogeneration

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import se.plilja.springdaogen.codegeneration.ClassGenerator
import se.plilja.springdaogen.generatedframework.baseEntity
import se.plilja.springdaogen.generatedframework.changedAtTracked
import se.plilja.springdaogen.generatedframework.createdAtTracked
import se.plilja.springdaogen.generatedframework.versionTracked
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Left
import se.plilja.springdaogen.model.Table
import java.time.LocalDate
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


fun generateEntity(config: Config, table: Table): ClassGenerator {
    val g = ClassGenerator(table.entityName(), config.entityOutputPackage, config.entityOutputFolder)
    g.addImplements("BaseEntity<${table.primaryKey.typeName()}>")
    if (config.useLombok) {
        g.addClassAnnotation("@Data")
        g.addClassAnnotation("@NoArgsConstructor")
        g.addClassAnnotation("@AllArgsConstructor")
        g.addImport(Data::class.java)
        g.addImport(AllArgsConstructor::class.java)
        g.addImport(NoArgsConstructor::class.java)
        g.hideConstructors = true
    }
    if (config.featureGenerateJacksonAnnotations) {
        g.addImport(JsonIgnoreProperties::class.java)
        g.addClassAnnotation("@JsonIgnoreProperties(ignoreUnknown = true)")
    }
    for (column in table.columns) {
        val type = column.type()
        when (type) {
            is Left -> g.addImport(type.value)
        }
        val annotations = ArrayList<String>()
        if (config.featureGenerateJavaxValidation) {
            val isSpecialColumn = table.primaryKey == column || // Will be null for new objects before they are saved
                    column.isVersionColumn() || // Should be set by framework and not by user
                    column.isCreatedAtColumn() || // Should be set by framework and not by user
                    column.isChangedAtColumn() // Should be set by framework and not by user
            if (!column.nullable && !isSpecialColumn) {
                g.addImport(NotNull::class.java)
                annotations.add("@NotNull")
            }
            if (column.type() == Left(String::class.java) && !column.isClobLike()) {
                g.addImport(Size::class.java)
                annotations.add("@Size(max = ${column.size})")
            }
        }
        if (config.featureGenerateJacksonAnnotations) {
            if (column.isVersionColumn()) {
                g.addImport(JsonIgnore::class.java)
                annotations.add("@JsonIgnore")
            }
        }

        if (config.useLombok) {
            g.addPrivateField(column.fieldName(), column.typeName(), annotations)
        } else if (column == table.primaryKey && column.name.toLowerCase() == "id") {
            g.addPrivateField(column.fieldName(), column.typeName(), annotations)
        } else if (column == table.createdAtColumn() && column.getter() == "getCreatedAt") {
            g.addPrivateField(column.fieldName(), column.typeName(), annotations)
        } else if (column == table.changedAtColumn() && column.getter() == "getChangedAt") {
            g.addPrivateField(column.fieldName(), column.typeName(), annotations)
        } else if (column == table.versionColumn() && column.getter() == "getVersion") {
            // Always use Integer for version column no matter what the db says
            g.addPrivateField(column.fieldName(), column.typeName(), annotations)
        } else {
            g.addField(column.fieldName(), column.typeName(), annotations)
        }
    }
    val idMethodGeneratedByLombok = config.useLombok && table.primaryKey.name == "id"
    if (!idMethodGeneratedByLombok) {
        val maybeJsonIgnore = if (config.featureGenerateJacksonAnnotations && table.primaryKey.name != "id") {
            g.addImport(JsonIgnore::class.java)
            "@JsonIgnore"
        } else {
            ""
        }
        g.addCustomMethod(
            """
        |   $maybeJsonIgnore
        |   @Override
        |   public ${table.primaryKey.typeName()} getId() {
        |       return ${table.primaryKey.fieldName()};
        |   }
    """.trimMargin()
        )
        g.addCustomMethod(
            """
        |   $maybeJsonIgnore
        |   @Override
        |   public void setId(${table.primaryKey.typeName()} id) {
        |       this.${table.primaryKey.fieldName()} = id;
        |   }
        """.trimMargin()
        )
    }

    val createdAtColumn = table.createdAtColumn()
    if (createdAtColumn != null) {
        ensureImported(g, config) { createdAtTracked(config.frameworkOutputPackage) }
        g.addImplements("CreatedAtTracked<${createdAtColumn.typeName()}>")
        if (!config.useLombok || createdAtColumn.getter() != "getCreatedAt") {
            val maybeJsonIgnore =
                if (config.featureGenerateJacksonAnnotations && createdAtColumn.getter() != "getCreatedAt") {
                    g.addImport(JsonIgnore::class.java)
                    "@JsonIgnore"
                } else {
                    ""
                }
            g.addCustomMethod(
                """
            |   $maybeJsonIgnore
            |   @Override
            |   public ${createdAtColumn.typeName()} getCreatedAt() {
            |       return ${createdAtColumn.fieldName()};
            |   }
            """.trimMargin()
            )
        }
        if (!config.useLombok) {
            val maybeJsonIgnore =
                if (config.featureGenerateJacksonAnnotations && createdAtColumn.setter() != "setCreatedAt") {
                    g.addImport(JsonIgnore::class.java)
                    "@JsonIgnore"
                } else {
                    ""
                }
            g.addCustomMethod(
                """
            |   $maybeJsonIgnore
            |   public void ${createdAtColumn.setter()}(${createdAtColumn.typeName()} value) {
            |       this.${createdAtColumn.fieldName()} = value;
            |   }
            """.trimMargin()
            )
        }

        g.addCustomMethod(
            """
        |   @Override
        |   public void setCreatedNow() {
        |       ${createdAtColumn.fieldName()} = ${if (createdAtColumn.rawType() == LocalDate::class.java) "LocalDate.now()" else "LocalDateTime.now()"};
        |   }
        """.trimMargin()
        )
    }

    val changedAtColumn = table.changedAtColumn()
    if (changedAtColumn != null) {
        ensureImported(g, config) { changedAtTracked(config.frameworkOutputPackage) }
        g.addImplements("ChangedAtTracked<${changedAtColumn.typeName()}>")
        if (!config.useLombok || changedAtColumn.getter() != "getChangedAt") {
            val maybeJsonIgnore =
                if (config.featureGenerateJacksonAnnotations && changedAtColumn.getter() != "getChangedAt") {
                    g.addImport(JsonIgnore::class.java)
                    "@JsonIgnore"
                } else {
                    ""
                }
            g.addCustomMethod(
                """
            |   $maybeJsonIgnore
            |   @Override
            |   public ${changedAtColumn.typeName()} getChangedAt() {
            |       return ${changedAtColumn.fieldName()};
            |   }
            """.trimMargin()
            )
        }
        if (!config.useLombok) {
            val maybeJsonIgnore =
                if (config.featureGenerateJacksonAnnotations && changedAtColumn.setter() != "setChangedAt") {
                    g.addImport(JsonIgnore::class.java)
                    "@JsonIgnore"
                } else {
                    ""
                }
            g.addCustomMethod(
                """
            |   $maybeJsonIgnore
            |   public void ${changedAtColumn.setter()}(${changedAtColumn.typeName()} value) {
            |       this.${changedAtColumn.fieldName()} = value;
            |   }
            """.trimMargin()
            )
        }

        g.addCustomMethod(
            """
        |   @Override
        |   public void setChangedNow() {
        |       ${changedAtColumn.fieldName()} = ${if (changedAtColumn.rawType() == LocalDate::class.java) "LocalDate.now()" else "LocalDateTime.now()"};
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
            |   public ${versionColumn.typeName()} getVersion() {
            |       return ${versionColumn.fieldName()};
            |   }
            """.trimMargin()
            )


            g.addCustomMethod(
                """
            |   @Override
            |   public void setVersion(${versionColumn.typeName()} value) {
            |       this.${versionColumn.fieldName()} = value;
            |   }
            """.trimMargin()
            )
        }
    }

    ensureImported(g, config) { baseEntity(config.frameworkOutputPackage) }

    return g
}

