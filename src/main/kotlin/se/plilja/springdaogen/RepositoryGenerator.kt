package se.plilja.springdaogen

import com.nurkiewicz.jdbcrepository.JdbcRepository
import com.nurkiewicz.jdbcrepository.RowUnmapper
import org.springframework.jdbc.core.RowMapper
import java.io.File


fun generateRepositories(schema: Schema) {
    for (table in schema.tables) {
        generateRepositoryForTable(table)
    }
}

fun generateRepositoryForTable(table: Table) {
    val g = ClassGenerator(table.repositoryName(), "generated")
    g.extends = "JdbcRepository<${table.entityName()}, Integer>" // TODO resolve from PK
    g.addImport(JdbcRepository::class.java)
    g.addConstant("ROW_MAPPER", "RowMapper<${table.entityName()}>", rowMapper(table))
    g.addConstant("ROW_UNMAPPER", "RowUnmapper<${table.entityName()}>", rowUnmapper(table))
    g.addImport(RowMapper::class.java)
    g.addImport(RowUnmapper::class.java)
    g.addImport(Map::class.java)
    g.addImport(HashMap::class.java)
    g.addCustomConstructor(
        """
        |    public ${g.name}() {
        |        super(ROW_MAPPER, ROW_UNMAPPER, "${table.name}", "${table.primaryKey.name}");
        |    }
    """.trimMargin()
    )
    g.addCustomMethod(
        """
        |    @Override
        |    protected <S extends ${table.entityName()}> S postCreate(S entity, Number generatedId) {
        |        entity.setId(generatedId.intValue());
        |        return entity;
        |    }
    """.trimMargin()
    )

    File(Config.outputFolder()).mkdirs()
    File(Config.outputFolder() + g.name + ".java").writeText(g.generate())

}

fun rowMapper(table: Table): String {
    val setters = table.columns.map { "                ${setterForColumn(table, it)}" }.joinToString("\n")
    return """(rs, i) -> {
        |        return new ${table.entityName()}()
        |$setters;
        |    }
    """.trimMargin()
}

fun setterForColumn(table: Table, column: Column) : String {
    return ".${column.setter()}((${column.javaType.simpleName}) rs.getObject(${table.constantsName()}.${column.constantsName()}))"
}

fun rowUnmapper(table: Table): String {
    val attributes = table.columns.map { "        m.put(\"${it.name}\", o.${it.getter()}());" }.joinToString("\n")
    return """o -> {
        |        Map<String, Object> m = new HashMap<>();
        |$attributes
        |        return m;
        |    }
    """.trimMargin()
}

