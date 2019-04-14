package se.plilja.springdaogen.engine.dao

import se.plilja.springdaogen.config.Config
import se.plilja.springdaogen.engine.model.Column
import se.plilja.springdaogen.engine.model.TableOrView
import se.plilja.springdaogen.engine.sql.formatIdentifier


fun columnConstantInitializer(tableOrView: TableOrView, column: Column, config: Config): Pair<String, String> {
    val name = column.name
    val escapedName = formatIdentifier(name, config.databaseDialect)
    val extra = if (name != escapedName) {
        ", \"$escapedName\""
    } else {
        ""
    }
    return when (column.typeName()) {
        "Boolean" -> Pair("Column.BooleanColumn<${tableOrView.entityName()}>", "new Column.BooleanColumn<>(\"$name\"$extra, \"${column.fieldName()}\")")
        "String" -> Pair("Column.StringColumn<${tableOrView.entityName()}>", "new Column.StringColumn<>(\"$name\"$extra, \"${column.fieldName()}\")")
        "Integer" -> Pair("Column.IntColumn<${tableOrView.entityName()}>", "new Column.IntColumn<>(\"$name\"$extra, \"${column.fieldName()}\")")
        "Long" -> Pair("Column.LongColumn<${tableOrView.entityName()}>", "new Column.LongColumn<>(\"$name\"$extra, \"${column.fieldName()}\")")
        "Double" -> Pair("Column.DoubleColumn<${tableOrView.entityName()}>", "new Column.DoubleColumn<>(\"$name\"$extra, \"${column.fieldName()}\")")
        "LocalDate" -> Pair("Column.DateColumn<${tableOrView.entityName()}>", "new Column.DateColumn<>(\"$name\"$extra, \"${column.fieldName()}\")")
        "LocalDateTime" -> Pair("Column.DateTimeColumn<${tableOrView.entityName()}>", "new Column.DateTimeColumn<>(\"$name\"$extra, \"${column.fieldName()}\")")
        "BigDecimal" -> Pair("Column.BigDecimalColumn<${tableOrView.entityName()}>", "new Column.BigDecimalColumn<>(\"$name\"$extra, \"${column.fieldName()}\")")
        else -> Pair("Column<${tableOrView.entityName()}, ${column.typeName()}>", "new Column<>(\"$name\"$extra, \"${column.fieldName()}\", ${column.typeName()}.class)")
    }
}
