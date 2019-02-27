package se.plilja.springdaogen.daogeneration

import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.sqlgeneration.formatIdentifier


fun columnConstantInitializer(column: Column, config: Config) =
        when (column.typeName()) {
            "Boolean" -> "new Column.BooleanColumn<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\")"
            "String" -> "new Column.StringColumn<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\")"
            "Integer" -> "new Column.IntColumn<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\")"
            "Long" -> "new Column.LongColumn<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\")"
            "Double" -> "new Column.DoubleColumn<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\")"
            "LocalDate" -> "new Column.DateColumn<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\")"
            "LocalDateTime" -> "new Column.DateTimeColumn<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\")"
            "BigDecimal" -> "new Column.BigDecimalColumn<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\")"
            else -> "new Column<>(\"${formatIdentifier(column.name, config.databaseDialect)}\", \"${column.fieldName()}\", ${column.typeName()}.class)"
        }
