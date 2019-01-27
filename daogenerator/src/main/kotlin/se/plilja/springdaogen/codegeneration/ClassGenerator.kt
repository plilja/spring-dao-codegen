package se.plilja.springdaogen.codegeneration

import se.plilja.springdaogen.util.capitalizeFirst


class ClassGenerator(
    name: String,
    packageName: String,
    sourceBaseFolder: String
) : ClassFileGenerator(name, sourceBaseFolder, packageName) {

    private val classAnnotations = ArrayList<String>()
    private val constants = ArrayList<Pair<Field, String>>()
    private val fields = ArrayList<Field>()
    private val customMethods = ArrayList<String>()
    var isFinal = false
    var isAbstract = false
    var extends: String? = null
    var implements: String? = null
    var isConstantsClass = false
    var hideConstructors = false
    private val customConstructors = ArrayList<String>()

    fun addClassAnnotation(annotation: String) {
        classAnnotations.add(annotation)
    }

    fun addConstant(name: String, type: Class<out Any>, initialization: String) {
        constants.add(Pair(Field(name, type.simpleName, false), initialization))
        addImport(type)
    }

    fun addPrivateConstant(name: String, type: String, initialization: String) {
        constants.add(Pair(Field(name, type, true), initialization))
    }

    fun addConstant(name: String, type: String, initialization: String) {
        constants.add(Pair(Field(name, type, false), initialization))
    }

    fun addPrivateField(name: String, type: Class<out Any>) {
        fields.add(Field(name, type.simpleName, true))
        addImport(type)
    }

    fun addField(name: String, type: Class<out Any>) {
        fields.add(Field(name, type.simpleName, false))
        addImport(type)
    }

    fun addCustomConstructor(constructor: String) {
        customConstructors.add(constructor)
    }

    fun setExtends(clazz: Class<out Any>) {
        this.extends = clazz.simpleName
        addImport(clazz)
    }

    fun addCustomMethod(methodCode: String) {
        customMethods.add(methodCode)
    }

    override fun generate(): String {
        val packageDeclaration = "package $packageName;"
        val joinedClassAnnotation = classAnnotations.joinToString("\n")
        val classHeader =
            "public${if (isAbstract) " abstract" else ""}${if (isFinal) " final" else ""} class $name${if (extends != null) " extends $extends" else ""}${if (implements != null) " implements $implements" else ""} {"
        val classDeclaration =
            if (joinedClassAnnotation.isEmpty()) {
                classHeader
            } else {
                joinedClassAnnotation + "\n" + classHeader
            }
        val constantsDeclaration =
            constants.map { "    ${it.first.getVisibility()} static final ${it.first.type} ${it.first.name} = ${it.second};" }
                .joinToString("\n")
        val fieldsDeclaration = fields.map { "    private ${it.type} ${it.name};" }.joinToString("\n")

        val noArgsConstructor =
            if (!hideConstructors and customConstructors.isEmpty())
                """${if (isConstantsClass) "private" else "public"} $name() {
                }"""
            else
                ""

        val allArgsConstructor =
            if (!hideConstructors and !isConstantsClass and customConstructors.isEmpty() and !fields.isEmpty())
                """public $name(${fields.map { "${it.type} ${it.name}" }.joinToString(", ")}) {
                ${fields.map { "this.${it.name} = ${it.name};" }.joinToString("\n")}
            }"""
            else
                ""

        val joinedCustomConstructors = customConstructors.joinToString("\n\n")

        val gettersAndSetters = fields.filter { !it.private }.map { getterAndSetter(it) }.joinToString("\n\n")
        val joinedCustomMethods = customMethods.joinToString("\n\n")
        val classClose = "}\n"

        val classParts = listOf(
            packageDeclaration,
            formatImports(),
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
        return formatCode(classParts.filter { !it.trim().isEmpty() }
            .joinToString("\n\n")
            .replace("    ", ""))

    }

    private fun getterAndSetter(field: Field): String {
        return """public ${field.type} get${capitalizeFirst(field.name)}() {
                      return ${field.name};
                  }

                  public void set${capitalizeFirst(field.name)}(${field.type} ${field.name}) {
                      this.${field.name} = ${field.name};
                  }"""
    }
}

data class Field(val name: String, val type: String, val private: Boolean) {
    fun getVisibility(): String {
        return if (private) {
            "private"
        } else {
            "public"
        }
    }
}