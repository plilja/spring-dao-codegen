package se.plilja.springdaogen

import org.springframework.data.domain.Persistable
import schemacrawler.schema.Catalog
import schemacrawler.schema.Column
import schemacrawler.schema.Table
import java.io.File


fun generateEntities(catalog: Catalog) {
    for (table in catalog.tables) {
        generateEntityForTable(table)
    }
}

fun generateEntityForTable(table: Table) {
    val g = ClassGenerator(capitalizeFirst(camelCase(table.name)) + "Entity", "generated")
    g.implements = "Persistable<Integer>" // TODO resolve from PK
    g.addImport(Persistable::class.java)
    for (column in table.columns) {
        val type = resolveType(column)
        g.addField(camelCase(column.name), type)
    }
    g.addCustomMethod("""
    public boolean isNew() {
        return getId() == null;
    }
    """.trimMargin())

    g.addCustomMethod("""
    public Integer getId() {
        return ${camelCase(table.primaryKey.columns[0].name)};
    }
    """.trimMargin())
    g.addCustomMethod("""
    public ${g.name} setId(Integer id) {
        return set${capitalizeFirst(camelCase(table.primaryKey.columns[0].name))}(id);
    }
    """.trimMargin())

    File(Config.outputFolder()).mkdirs()
    File(Config.outputFolder() + g.name + ".java").writeText(g.generate())
}




