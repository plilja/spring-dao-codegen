package se.plilja.springdaogen

import java.util.*
import kotlin.collections.ArrayList


class ClassGenerator(
    val name: String,
    val packageName: String
) {
    private val imports = TreeSet<Class<out Any>> { a, b -> a.canonicalName.compareTo(b.canonicalName) }
    private val classAnnotations = ArrayList<String>()
    private val constants = ArrayList<Pair<Field, String>>()
    private val fields = ArrayList<Field>()
    private val customMethods = ArrayList<String>()
    var isFinal = false
    var extends: String? = null
    var implements: String? = null
    var isConstantsClass = false
    private val customConstructors = ArrayList<String>()

    fun addClassAnnotation(annotation: String) {
        classAnnotations.add(annotation)
    }

    fun addConstant(name: String, type: Class<out Any>, initialization: String) {
        constants.add(Pair(Field(name, type.simpleName), initialization))
        addImport(type)
    }

    fun addConstant(name: String, type: String, initialization: String) {
        constants.add(Pair(Field(name, type), initialization))
    }

    fun addField(name: String, type: Class<out Any>) {
        fields.add(Field(name, type.simpleName))
        addImport(type)
    }

    fun addCustomConstructor(constructor: String) {
        customConstructors.add(constructor)
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
        val joinedClassAnnotation = classAnnotations.joinToString("\n")
        val classHeader =
            "public${if (isFinal) " final" else ""} class $name${if (extends != null) " extends $extends" else ""}${if (implements != null) " implements $implements" else ""} {"
        val classDeclaration =
            if (joinedClassAnnotation.isEmpty()) {
                classHeader
            } else {
                joinedClassAnnotation + "\n" + classHeader
            }
        val constantsDeclaration =
            constants.map { "    public static final ${it.first.type} ${it.first.name} = ${it.second};" }
                .joinToString("\n")
        val fieldsDeclaration = fields.map { "    private ${it.type} ${it.name};" }.joinToString("\n")

        val noArgsConstructor =
            if (customConstructors.isEmpty())
                """    ${if (isConstantsClass) "private" else "public"} $name() {
    }"""
            else
                ""

        val allArgsConstructor = if (!isConstantsClass and customConstructors.isEmpty() and !fields.isEmpty())
            """    public $name(${fields.map { "${it.type} ${it.name}" }.joinToString(", ")}) {
${fields.map { "        this.${it.name} = ${it.name};" }.joinToString("\n")}
    }"""
        else
            ""

        val joinedCustomConstructors = customConstructors.joinToString("\n\n")

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
            allArgsConstructor,
            joinedCustomConstructors,
            gettersAndSetters,
            joinedCustomMethods,
            classClose
        )
        return classParts.filter { !it.trim().isEmpty() }
            .joinToString("\n\n")

    }

    private fun getterAndSetter(field: Field): String {
        return """    public ${field.type} get${capitalizeFirst(field.name)}() {
        return ${field.name};
    }

    public $name set${capitalizeFirst(field.name)}(${field.type} ${field.name}) {
        this.${field.name} = ${field.name};
        return this;
    }"""
    }

}

data class Field(val name: String, val type: String) {
}