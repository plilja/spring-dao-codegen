package se.plilja.springdaogen.codegeneration


class InterfaceGenerator(
    name: String,
    packageName: String,
    sourceBaseFolder: String
) : ClassFileGenerator(name, sourceBaseFolder, packageName) {

    var extends: String? = null

    override fun generate(): String {
        val packageDeclaration = "package $packageName;"
        val interfaceDeclaration = "public interface $name ${if (extends != null) "extends $extends " else ""}{"
        val close = "}"
        val classParts = listOf(
            packageDeclaration,
            formatImports(),
            interfaceDeclaration,
            close
        )
        return formatCode(classParts.filter { !it.trim().isEmpty() }
            .joinToString("\n\n")
            .replace("    ", ""))
    }

}