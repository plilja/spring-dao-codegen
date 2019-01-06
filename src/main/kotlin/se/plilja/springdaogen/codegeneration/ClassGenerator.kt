package se.plilja.springdaogen.codegeneration

import se.plilja.springdaogen.util.capitalizeFirst
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
        constants.add(Pair(Field(name, type.simpleName, false), initialization))
        addImport(type)
    }

    fun addPrivateConstant(name: String, type: String, initialization: String) {
        constants.add(Pair(Field(name, type, true), initialization))
    }

    fun addConstant(name: String, type: String, initialization: String) {
        constants.add(Pair(Field(name, type, false), initialization))
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
            imports.add(type)
        }
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
            constants.map { "    ${it.first.getVisibility()} static final ${it.first.type} ${it.first.name} = ${it.second};" }
                .joinToString("\n")
        val fieldsDeclaration = fields.map { "    private ${it.type} ${it.name};" }.joinToString("\n")

        val noArgsConstructor =
            if (customConstructors.isEmpty())
                """${if (isConstantsClass) "private" else "public"} $name() {
                }"""
            else
                ""

        val allArgsConstructor = if (!isConstantsClass and customConstructors.isEmpty() and !fields.isEmpty())
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

                  public $name set${capitalizeFirst(field.name)}(${field.type} ${field.name}) {
                      this.${field.name} = ${field.name};
                      return this;
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
        val extraIndent = if (prevLine != "" && !(prevLine.last() in listOf(';', '{', '}')) && prevLine[0] != '@') {
            tab + tab
        } else {
            ""
        }

        if (line.endsWith("}") || line.endsWith("};")) {
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

//        var res = ""
//    var ind = 0
//    var prevStatementTerminated = false
//    val tab = "    "
//    for (i in 0 until s.length) {
//        val c = s[i]
//        val lookAhead = if (i + 1 < s.length) s[i + 1] else '-'
//        if (c == '{') {
//            ind += 1
//        } else if (c == '}') {
//            ind = Math.max(0, ind - 1)
//        } else if (c == ';') {
//            prevStatementTerminated = true
//        }
//        res += c
//        if (c == '\n') {
//            val lim = if (lookAhead != '}') ind else Math.max(0, ind - 1)
//            for (j in 1..lim) {
//                res += tab
//            }
//            prevStatementTerminated = true;
//        }
//    }
//    return res
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