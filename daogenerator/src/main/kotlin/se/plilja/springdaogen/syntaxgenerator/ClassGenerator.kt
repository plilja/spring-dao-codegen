package se.plilja.springdaogen.syntaxgenerator


class ClassGenerator(
    name: String,
    packageName: String,
    sourceBaseFolder: String
) : AbstractClassGenerator(name, packageName, sourceBaseFolder) {

    private val constants = ArrayList<Pair<Field, String>>()
    private var isFinal = false
    var isAbstract = false
    var extends: String? = null
    var isConstantsClass = false
    var hideConstructors = false
    private val customConstructors = ArrayList<String>()

    fun addCustomConstructor(constructor: String) {
        customConstructors.add(constructor)
    }

    fun setExtends(clazz: Class<out Any>) {
        this.extends = clazz.simpleName
        addImport(clazz)
    }

    fun addConstant(name: String, type: Class<out Any>, initialization: String) {
        constants.add(Pair(Field(name, type.simpleName, false, true, emptyList()), initialization))
        addImport(type)
    }

    fun addPrivateConstant(name: String, type: String, initialization: String) {
        constants.add(Pair(Field(name, type, true, true, emptyList()), initialization))
    }

    fun addConstant(name: String, type: String, initialization: String) {
        constants.add(Pair(Field(name, type, false, true, emptyList()), initialization))
    }

    override fun generate(): String {
        val packageDeclaration = "package $packageName;"
        val importsDeclaration = imports.map { "import $it;" }.joinToString("\n")
        val joinedClassAnnotation = classAnnotations.joinToString("\n")
        val implementsDecl = if (implements.isEmpty()) "" else " implements " + implements.joinToString(", ")
        val classHeader =
            "public${if (isAbstract) " abstract" else ""}${if (isFinal) " final" else ""} class $name${if (extends != null) " extends $extends" else ""}$implementsDecl {"
        val classDeclaration =
            if (joinedClassAnnotation.isEmpty()) {
                classHeader
            } else {
                joinedClassAnnotation + "\n" + classHeader
            }
        val constantsDeclaration =
            constants.map { "    ${it.first.getVisibility()} static final ${it.first.type} ${it.first.name} = ${it.second};" }
                .joinToString("\n\n")
        val fieldsDeclaration =
            fields.map { "${it.annotations.map { "$it\n" }.joinToString("")}private ${it.type} ${it.name};" }
                .joinToString("\n")

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
        return rightTrimLines(indent(classParts.filter { !it.trim().isEmpty() }
            .joinToString("\n\n")
            .replace("    ", "")))

    }

}
