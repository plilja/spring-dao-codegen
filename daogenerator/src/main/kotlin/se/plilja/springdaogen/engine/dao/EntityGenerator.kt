package se.plilja.springdaogen.engine.dao

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import se.plilja.springdaogen.classgenerators.ClassGenerator
import se.plilja.springdaogen.config.Config
import se.plilja.springdaogen.copyable.*
import se.plilja.springdaogen.engine.model.Left
import se.plilja.springdaogen.engine.model.Table
import java.time.LocalDate
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


fun generateEntity(config: Config, table: Table): ClassGenerator {
    println("Generating entity for table '${table.name}', entity will be named '${table.entityName()}'.")
    val g = ClassGenerator(table.entityName(), config.entityOutputPackage, config.entityOutputFolder)
    g.addImplements("BaseEntity<${table.primaryKey.typeName()}>")
    if (config.featuresUseLombok) {
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
                if (column.size < Integer.MAX_VALUE) {
                    g.addImport(Size::class.java)
                    annotations.add("@Size(max = ${column.size})")
                }
            }
        }
        if (config.featureGenerateJacksonAnnotations) {
            if (column.isVersionColumn()) {
                g.addImport(JsonIgnore::class.java)
                annotations.add("@JsonIgnore")
            }
        }

        if (config.featuresUseLombok) {
            g.addPrivateField(column.fieldName(), column.typeName(), annotations)
        } else if (column == table.primaryKey && column.name.toLowerCase() == "id") {
            g.addPrivateField(column.fieldName(), column.typeName(), annotations)
        } else if (config.featureGenerateChangeTracking && column == table.createdAtColumn() && column.getter() == "getCreatedAt") {
            g.addPrivateField(column.fieldName(), column.typeName(), annotations)
        } else if (config.featureGenerateChangeTracking && column == table.changedAtColumn() && column.getter() == "getChangedAt") {
            g.addPrivateField(column.fieldName(), column.typeName(), annotations)
        } else if (config.featureGenerateChangeTracking && column == table.versionColumn() && column.getter() == "getVersion") {
            g.addPrivateField(column.fieldName(), column.typeName(), annotations)
        } else if (config.featureGenerateChangeTracking && column == table.changedByColumn() && column.getter() == "getChangedBy") {
            g.addPrivateField(column.fieldName(), column.typeName(), annotations)
        } else if (config.featureGenerateChangeTracking && column == table.createdByColumn() && column.getter() == "getCreatedBy") {
            g.addPrivateField(column.fieldName(), column.typeName(), annotations)
        } else {
            g.addField(column.fieldName(), column.typeName(), annotations)
        }
    }
    val idMethodGeneratedByLombok = config.featuresUseLombok && table.primaryKey.name == "id"
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

    if (config.featureGenerateChangeTracking) {
        generatedChangeTracking(table, g, config)
    }
    if (!config.featuresUseLombok) {
        val nameColumnOrNull = table.nameColumn()
        val maybeNameColumn = if (nameColumnOrNull != null) {
            "\", ${nameColumnOrNull.fieldName()}=\" + ${nameColumnOrNull.fieldName()} + "
        } else {
            ""
        }
        g.addCustomMethod(
                """
        |   @Override
        |   public String toString() {
        |       return "${table.entityName()}{${table.primaryKey.fieldName()}=" + ${table.primaryKey.fieldName()} + $maybeNameColumn"}";
        |   }
        """.trimMargin())
    }

    ensureImported(g, config) { baseEntity(config.frameworkOutputPackage) }

    return g
}

private fun generatedChangeTracking(
    table: Table,
    g: ClassGenerator,
    config: Config
) {
    val createdAtColumn = table.createdAtColumn()
    if (createdAtColumn != null) {
        ensureImported(g, config) { createdAtTracked(config.frameworkOutputPackage) }
        g.addImplements("CreatedAtTracked<${createdAtColumn.typeName()}>")
        if (!config.featuresUseLombok || createdAtColumn.getter() != "getCreatedAt") {
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
        if (!config.featuresUseLombok && createdAtColumn.setter() == "setCreatedAt") {
            g.addCustomMethod(
                """
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
        if (!config.featuresUseLombok || changedAtColumn.getter() != "getChangedAt") {
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
        if (!config.featuresUseLombok && changedAtColumn.setter() == "setChangedAt") {
            g.addCustomMethod(
                """
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

    val createdByColumn = table.createdByColumn()
    if (createdByColumn != null) {
        ensureImported(g, config) { createdByTracked(config.frameworkOutputPackage) }
        g.addImplements("CreatedByTracked")
        if (!config.featuresUseLombok || createdByColumn.getter() != "getCreatedBy") {
            val maybeJsonIgnore =
                if (config.featureGenerateJacksonAnnotations && createdByColumn.getter() != "getCreatedBy") {
                    g.addImport(JsonIgnore::class.java)
                    "@JsonIgnore"
                } else {
                    ""
                }
            g.addCustomMethod(
                """
            |   $maybeJsonIgnore
            |   @Override
            |   public String getCreatedBy() {
            |       return ${createdByColumn.fieldName()};
            |   }
            """.trimMargin()
            )
            g.addCustomMethod(
                """
            |   $maybeJsonIgnore
            |   @Override
            |   public void setCreatedBy(String value) {
            |       this.${createdByColumn.fieldName()} = value;
            |   }
            """.trimMargin()
            )
        }
    }

    val changedByColumn = table.changedByColumn()
    if (changedByColumn != null) {
        ensureImported(g, config) { changedByTracked(config.frameworkOutputPackage) }
        g.addImplements("ChangedByTracked")
        if (!config.featuresUseLombok || changedByColumn.getter() != "getChangedBy") {
            val maybeJsonIgnore =
                if (config.featureGenerateJacksonAnnotations && changedByColumn.getter() != "getChangedBy") {
                    g.addImport(JsonIgnore::class.java)
                    "@JsonIgnore"
                } else {
                    ""
                }
            g.addCustomMethod(
                """
            |   $maybeJsonIgnore
            |   @Override
            |   public String getChangedBy() {
            |       return ${changedByColumn.fieldName()};
            |   }
            """.trimMargin()
            )
            g.addCustomMethod(
                """
            |   $maybeJsonIgnore
            |   @Override
            |   public void setChangedBy(String value) {
            |       this.${changedByColumn.fieldName()} = value;
            |   }
            """.trimMargin()
            )
        }
    }

    val versionColumn = table.versionColumn()
    if (versionColumn != null) {
        ensureImported(g, config) { versionTracked(config.frameworkOutputPackage) }
        g.addImplements("VersionTracked")
        if (!config.featuresUseLombok || versionColumn.getter() != "getVersion") {
            val maybeJsonIgnore =
                    if (config.featureGenerateJacksonAnnotations) {
                        g.addImport(JsonIgnore::class.java)
                        "\n   @JsonIgnore"
                    } else {
                        ""
                    }
            g.addCustomMethod(
                """
            |   @Override$maybeJsonIgnore
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
}


