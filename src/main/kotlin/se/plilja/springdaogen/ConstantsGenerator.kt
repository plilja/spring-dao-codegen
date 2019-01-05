package se.plilja.springdaogen

import schemacrawler.schema.Catalog
import schemacrawler.schema.Table


fun generateConstants(catalog: Catalog) {
    for (table in catalog.tables) {
        if (table.type.isView) {
            generateConstantsForView(table)
        } else {
            generateConstantsForTable(table)
        }
    }

}

fun generateConstantsForView(view: Table) {
}

fun generateConstantsForTable(table: Table) {
}

