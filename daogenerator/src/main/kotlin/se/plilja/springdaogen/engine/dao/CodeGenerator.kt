package se.plilja.springdaogen.engine.dao

import se.plilja.springdaogen.config.Config
import se.plilja.springdaogen.engine.model.Schema
import se.plilja.springdaogen.engine.model.TableContents
import se.plilja.springdaogen.syntaxgenerator.AbstractClassGenerator

fun generateCode(config: Config, schemas: List<Schema>, tableContents: TableContents): List<AbstractClassGenerator> {
    val tableClasses = schemas.flatMap { schema ->
        schema.tables.flatMap { table ->
            val list = ArrayList<AbstractClassGenerator>()
            if (table.isEnum()) {
                val rows = tableContents.getContents(table)
                list.add(generateEnums(config, table, rows))
            } else {
                list.add(generateDao(config, table))
                list.add(generateEntity(config, table))
            }
            list
        }
    }
    val viewClasses = schemas.flatMap { schema ->
        schema.views.flatMap { table ->
            val list = ArrayList<AbstractClassGenerator>()
            list.add(generateViewQueryable(config, table))
            list.add(generateViewEntity(config, table))
            list
        }
    }
    return tableClasses + viewClasses
}

