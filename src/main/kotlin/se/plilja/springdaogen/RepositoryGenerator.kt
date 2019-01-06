package se.plilja.springdaogen

import com.nurkiewicz.jdbcrepository.JdbcRepository
import com.nurkiewicz.jdbcrepository.RowUnmapper
import org.springframework.jdbc.core.RowMapper
import schemacrawler.schema.Catalog
import schemacrawler.schema.Column
import schemacrawler.schema.Table
import java.io.File


fun generateRepositories(catalog: Catalog) {
    for (table in catalog.tables) {
        generateRepositoryForTable(table)
    }
}

fun generateRepositoryForTable(table: Table) {
    val entity = "${camelCase(table.name)}Entity"
    val constants = "${camelCase(table.name)}Db"
    val g = ClassGenerator(capitalizeFirst(camelCase(table.name)) + "Repository", "generated")
    g.extends = "JdbcRepository<$entity, Integer>" // TODO resolve from PK
    g.addImport(JdbcRepository::class.java)
    g.addConstant("ROW_MAPPER", "RowMapper<$entity>", rowMapper(table, entity, constants))
    g.addConstant("ROW_UNMAPPER", "RowUnmapper<$entity>", rowUnmapper(table))
    g.addImport(RowMapper::class.java)
    g.addImport(RowUnmapper::class.java)
    g.addImport(Map::class.java)
    g.addImport(HashMap::class.java)
    g.addCustomConstructor(
        """
        |    public ${g.name}() {
        |        super(ROW_MAPPER, ROW_UNMAPPER, "${table.name}", "${table.primaryKey.columns[0].name}");
        |    }
    """.trimMargin()
    )
    g.addCustomMethod(
        """
        |    @Override
        |    protected <S extends $entity> S postCreate(S entity, Number generatedId) {
        |        entity.setId(generatedId.intValue());
        |        return entity;
        |    }
    """.trimMargin()
    )

    File(Config.outputFolder()).mkdirs()
    File(Config.outputFolder() + g.name + ".java").writeText(g.generate())

}

fun rowMapper(table: Table, entityName: String, constants: String): String {
    val setters = table.columns.map { "                ${setterForColumn(it, constants)}" }.joinToString("\n")
    return """(rs, i) -> {
        |        return new $entityName()
        |$setters;
        |    }
    """.trimMargin()
}

fun setterForColumn(column: Column, constants: String) : String {
    val setterMethod = "set${capitalizeFirst(camelCase(column.name))}"
    val constant = "$constants.${snakeCase(column.name).toUpperCase()}"
    return ".$setterMethod((${resolveType(column).simpleName}) rs.getObject($constant))"
}

fun rowUnmapper(table: Table): String {
    val attributes = table.columns.map { "        m.put(\"${it.name}\", o.${getterForColumn(it)});" }.joinToString("\n")
    return """o -> {
        |        Map<String, Object> m = new HashMap<>();
        |$attributes
        |        return m;
        |    }
    """.trimMargin()
}

fun getterForColumn(column: Column) : String {
    val getterMethod = "get${capitalizeFirst(camelCase(column.name))}"
    return "$getterMethod()"
}
