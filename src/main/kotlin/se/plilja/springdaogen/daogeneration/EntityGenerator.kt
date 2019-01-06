package se.plilja.springdaogen.daogeneration

import org.springframework.data.domain.Persistable
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Table
import se.plilja.springdaogen.codegeneration.ClassGenerator


fun generateEntity(config: Config, table: Table) : ClassGenerator {
    val g = ClassGenerator(table.entityName(), config.outputPackage)
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
    return g
}
