package se.plilja.springdaogen

import java.util.*


class ClassGenerator(
    val name: String,
    val packageName: String
) {
    val imports = TreeSet<Class<out Any>> { a, b -> a.canonicalName.compareTo(b.canonicalName) }
    val constants = ArrayList<Pair<Field, String>>()
    val fields = ArrayList<Field>()
    val customMethods = ArrayList<String>()
    var isFinal = false
    var extends: String? = null
    var implements: String? = null
    var isConstantsClass = false

    fun addConstant(name: String, type: Class<out Any>, initialization: String) {
        constants.add(Pair(Field(name, type), initialization))
        addImport(type)
    }

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

    fun addCustomMethod(methodCode: String) {
        customMethods.add(methodCode)
    }

    fun generate(): String {
        val packageDeclaration = "package $packageName;"
        val importsDeclaration = imports.map { "import ${it.canonicalName};" }.joinToString("\n")
        val classDeclaration =
            "public${if (isFinal) " final" else ""} class $name${if (extends != null) " extends $extends" else ""}${if (implements != null) " implements $implements" else ""} {"
        val constantsDeclaration = constants.map { "    public static final ${it.first.type.simpleName} ${it.first.name} = ${it.second};" }.joinToString("\n")
        val fieldsDeclaration = fields.map { "    private ${it.type.simpleName} ${it.name};" }.joinToString("\n")
        val noArgsConstructor = """    ${if (isConstantsClass) "private" else "public"} $name() {
    }"""
        val allArgsConstructor = if (!isConstantsClass)
            """    public $name(${fields.map { "${it.type.simpleName} ${it.name}" }.joinToString(", ")}) {
${fields.map { "        this.${it.name} = ${it.name};" }.joinToString("\n")}
    }""" else ""
        val gettersAndSetters = fields.map { getterAndSetter(it) }.joinToString("\n\n")
        val joinedCustomMethods = customMethods.joinToString("\n\n")
        val classClose = "}\n"

        val classParts = listOf(
            packageDeclaration,
            importsDeclaration,
            classDeclaration,
            constantsDeclaration,
            fieldsDeclaration,
            noArgsConstructor,
            if (!fields.isEmpty()) allArgsConstructor else "",
            gettersAndSetters,
            joinedCustomMethods,
            classClose
        )
        return classParts.filter { !it.trim().isEmpty() }
            .joinToString("\n\n")

    }

    private fun getterAndSetter(field: Field): String {
        return """    public ${field.type.simpleName} get${capitalizeFirst(field.name)}() {
        return ${field.name};
    }

    public $name set${capitalizeFirst(field.name)}(${field.type.simpleName} ${field.name}) {
        this.${field.name} = ${field.name};
        return this;
    }"""
    }

}

data class Field(val name: String, val type: Class<out Any>) {
}