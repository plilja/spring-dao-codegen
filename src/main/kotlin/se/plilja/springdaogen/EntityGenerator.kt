package se.plilja.springdaogen

import org.springframework.data.domain.Persistable
import java.io.File


fun generateEntities(schema: Schema) {
    for (table in schema.tables) {
        generateEntityForTable(table)
    }
}

fun generateEntityForTable(table: Table) {
    val g = ClassGenerator(table.entityName(), "generated")
    g.implements = "Persistable<Integer>" // TODO resolve from PK
    g.addImport(Persistable::class.java)
    for (column in table.columns) {
        g.addField(column.fieldName(), column.javaType)
    }
    g.addCustomMethod("""
    public boolean isNew() {
        return getId() == null;
    }
    """.trimMargin())

    g.addCustomMethod("""
    public Integer getId() {
        return ${table.primaryKey.fieldName()};
    }
    """.trimMargin())
    g.addCustomMethod("""
    public ${g.name} setId(Integer id) {
        return ${table.primaryKey.setter()}(id);
    }
    """.trimMargin())

    File(Config.outputFolder()).mkdirs()
    File(Config.outputFolder() + g.name + ".java").writeText(g.generate())
}




