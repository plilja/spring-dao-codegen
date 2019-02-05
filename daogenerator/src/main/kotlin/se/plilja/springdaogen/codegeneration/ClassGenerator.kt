package se.plilja.springdaogen.codegeneration

import se.plilja.springdaogen.util.capitalizeFirst
import java.util.*
import kotlin.collections.ArrayList


class ClassGenerator(
    val name: String,
    val packageName: String,
    val sourceBaseFolder: String
) {
    private val imports = TreeSet<String>()
    private val classAnnotations = ArrayList<String>()
    private val constants = ArrayList<Pair<Field, String>>()
    private val fields = ArrayList<Field>()
    private val customMethods = ArrayList<String>()
    var isFinal = false
    var isAbstract = false
    var extends: String? = null
    private var implements = ArrayList<String>()
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

    fun addImplements(iface: String) {
        implements.add(iface)
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

    fun addImport(type: Class<out Any>) {
        if (!type.packageName.startsWith("java.lang")) {
            imports.add(type.canonicalName)
        }
    }

    fun addImport(canonicalName: String) {
        if (!canonicalName.startsWith("java.lang.")) {
            imports.add(canonicalName)
        }
    }

    fun setExtends(clazz: Class<out Any>) {
        this.extends = clazz.simpleName
        addImport(clazz)
    }

    fun addCustomMethod(methodCode: String) {
        customMethods.add(methodCode)
    }

    fun getOutputFolder(): String {
        return "${sourceBaseFolder}${packageName.replace(".", "/")}"
    }

    fun getOutputFileName(): String {
        return "${getOutputFolder()}/${name}.java"
    }

    fun generate(): String {
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

    private fun getterAndSetter(field: Field): String {
        return """public ${field.type} get${capitalizeFirst(field.name)}() {
                      return ${field.name};
                  }

                  public void set${capitalizeFirst(field.name)}(${field.type} ${field.name}) {
                      this.${field.name} = ${field.name};
                  }"""
    }
}

fun indent(code: String): String {
    val lines = code.split("\n").map { it.trim() }
    val tab = "    "
    var indent = 0
    var res = lines[0]
    for (i in 1 until lines.size) {
        val prevLine = lines[i - 1]
        val line = lines[i]
        if (prevLine.trim() == "" && line == "") {
            continue
        }
        val extraIndent = if (prevLine != "" && prevLine.last() !in listOf(';', '{', '}') && !prevLine.startsWith("//") && prevLine[0] != '@') {
            tab + tab
        } else {
            ""
        }

        if (line.endsWith("}") || line.endsWith("};") || line.startsWith("}")) {
            indent -= 1
        }

        var spaces = ""
        for (j in 1..indent) {
            spaces += tab
        }
        if (line.endsWith("{")) {
            indent += 1
        }

        res += "\n" + extraIndent + spaces + line
    }
    return res
}

fun rightTrimLines(s: String): String {
    return s.split("\n").map { it.trimEnd() }.joinToString("\n")
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