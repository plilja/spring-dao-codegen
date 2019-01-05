package se.plilja.springdaogen

import java.util.*


class ClassGenerator(
    val name: String,
    val packageName: String
) {
    val imports = TreeSet<Class<out Any>> { a, b -> a.canonicalName.compareTo(b.canonicalName) }
    val fields = ArrayList<Field>()
    var isFinal = false
    var extends: String? = null

    fun addField(name: String, type: Class<out Any>) {
        fields.add(Field(name, type))
        addImport(type)
    }

    fun addImport(type: Class<out Any>) {
        imports.add(type)
    }

    fun setExtends(clazz: Class<out Any>) {
        this.extends = clazz.simpleName
        addImport(clazz)
    }

    fun generate(): String {
        val packageDeclaration = "package ${packageName};"
        val importsDeclaration = imports.map { "import ${it.canonicalName};" }.joinToString("\n")
        val classDeclaration =
            "public${if (isFinal) " final" else ""} class ${name}${if (extends != null) " extends ${extends}" else ""} {"
        val fieldsDeclaration = fields.map { "    private ${it.type.simpleName} ${it.name};" }.joinToString("\n")
        val noArgsConstructor = """    public ${name}() {
    }"""
        val allArgsConstructor =
            """    public ${name}(${fields.map { "${it.type.simpleName} ${it.name}" }.joinToString(", ")}) {
${fields.map { "        this.${it.name} = ${it.name};" }.joinToString("\n")}
    }"""
        val gettersAndSetters = fields.map { getterAndSetter(it) }.joinToString("\n\n")
        val classClose = "}\n"

        val classParts = listOf(
            packageDeclaration,
            importsDeclaration,
            classDeclaration,
            fieldsDeclaration,
            noArgsConstructor,
            if (!fields.isEmpty()) allArgsConstructor else "",
            gettersAndSetters,
            classClose
        )
        return classParts.filter { !it.trim().isEmpty() }
            .joinToString("\n\n")

    }

    private fun getterAndSetter(field: Field): String {
        return """    public ${field.type.simpleName} get${capitalizeFirst(field.name)}() {
        return ${field.name};
    }

    public ${name} set${capitalizeFirst(field.name)}(${field.type.simpleName} ${field.name}) {
        this.${field.name} = ${field.name};
        return this;
    }"""
    }

    private fun capitalizeFirst(s: String): String {
        if (s.isEmpty()) {
            return ""
        } else {
            return s.get(0).toUpperCase() + s.substring(1)
        }
    }
}

data class Field(val name: String, val type: Class<out Any>) {
}