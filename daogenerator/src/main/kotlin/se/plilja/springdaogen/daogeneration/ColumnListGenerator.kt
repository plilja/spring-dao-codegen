package se.plilja.springdaogen.daogeneration

import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.TableOrView
import se.plilja.springdaogen.sqlgeneration.formatIdentifier


fun columnConstantInitializer(tableOrView: TableOrView, column: Column, config: Config) =
        when (column.typeName()) {
            "Boolean" -> Pair("Column.BooleanColumn<${tableOrView.entityName()}>", "new Column.BooleanColumn<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\")")
            "String" -> Pair("Column.StringColumn<${tableOrView.entityName()}>", "new Column.StringColumn<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\")")
            "Integer" -> Pair("Column.IntColumn<${tableOrView.entityName()}>", "new Column.IntColumn<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\")")
            "Long" -> Pair("Column.LongColumn<${tableOrView.entityName()}>", "new Column.LongColumn<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\")")
            "Double" -> Pair("Column.DoubleColumn<${tableOrView.entityName()}>", "new Column.DoubleColumn<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\")")
            "LocalDate" -> Pair("Column.DateColumn<${tableOrView.entityName()}>", "new Column.DateColumn<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\")")
            "LocalDateTime" -> Pair("Column.DateTimeColumn<${tableOrView.entityName()}>", "new Column.DateTimeColumn<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\")")
            "BigDecimal" -> Pair("Column.BigDecimalColumn<${tableOrView.entityName()}>", "new Column.BigDecimalColumn<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\")")
            else -> Pair("Column<${tableOrView.entityName()}, ${column.typeName()}>", "new Column<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\", ${column.typeName()}.class)")
        }
