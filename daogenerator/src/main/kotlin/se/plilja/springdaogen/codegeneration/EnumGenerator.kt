package se.plilja.springdaogen.codegeneration


class EnumGenerator(
    name: String,
    packageName: String,
    sourceBaseFolder: String
) : AbstractClassGenerator(name, packageName, sourceBaseFolder) {

    private val enumValues = ArrayList<String>()

    fun addEnumValue(value: String) {
        enumValues.add(value)
    }

    override fun generate(): String {
        val packageDeclaration = "package $packageName;"
        val importsDeclaration = imports.map { "import $it;" }.joinToString("\n")
        val classHeader = "public enum $name {"

        val enumValuesDecl = enumValues.joinToString(",\n") + ";"

        val fieldsDeclaration = fields.map { "private final ${it.type} ${it.name};" }.joinToString("\n")

        val allArgsConstructor = """$name(${fields.map { "${it.type} ${it.name}" }.joinToString(", ")}) {
                ${fields.map { "this.${it.name} = ${it.name};" }.joinToString("\n")}
            }"""

        val gettersAndSetters = fields.filter { !it.private }.map { getterAndSetter(it) }.joinToString("\n\n")
        val joinedCustomMethods = customMethods.joinToString("\n\n")
        val classClose = "}\n"

        val classParts = listOf(
            packageDeclaration,
            importsDeclaration,
            classHeader,
            enumValuesDecl,
            fieldsDeclaration,
            allArgsConstructor,
            gettersAndSetters,
            joinedCustomMethods,
            classClose
        )
        return rightTrimLines(indent(classParts.filter { !it.trim().isEmpty() }
            .joinToString("\n\n")
            .replace("    ", "")))

    }
}