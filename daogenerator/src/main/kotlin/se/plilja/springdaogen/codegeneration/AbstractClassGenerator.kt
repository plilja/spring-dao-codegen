package se.plilja.springdaogen.codegeneration

import se.plilja.springdaogen.util.capitalizeFirst
import java.util.*


abstract class AbstractClassGenerator(
        val name: String,
        val packageName: String,
        private val sourceBaseFolder: String
) {
    protected val imports = TreeSet<String>()
    protected val classAnnotations = ArrayList<String>()
    protected val fields = ArrayList<Field>()
    protected val customMethods = ArrayList<String>()
    protected var implements = ArrayList<String>()

    abstract fun generate(): String

    fun getOutputFolder(): String {
        return "$sourceBaseFolder${packageName.replace(".", "/")}"
    }

    fun getOutputFileName(): String {
        return "${getOutputFolder()}/$name.java"
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

    fun addClassAnnotation(annotation: String) {
        classAnnotations.add(annotation)
    }

    fun addImplements(iface: String) {
        implements.add(iface)
    }

    fun addPrivateField(name: String, type: String) {
        fields.add(Field(name, type, true, false, emptyList()))
    }

    fun addReadOnlyField(name: String, type: String) {
        fields.add(Field(name, type, false, true, emptyList()))
    }

    fun addField(name: String, type: Class<out Any>) {
        fields.add(Field(name, type.simpleName, false, false, emptyList()))
        addImport(type)
    }

    fun addPrivateField(name: String, type: String, annotations: List<String>) {
        fields.add(Field(name, type, true, false, annotations))
    }

    fun addField(name: String, type: String, annotations: List<String>) {
        fields.add(Field(name, type, false, false, annotations))
    }

    fun addCustomMethod(methodCode: String) {
        customMethods.add(methodCode)
    }

    protected fun getterAndSetter(field: Field): String {
        val getter = """
            public ${field.type} get${capitalizeFirst(field.name)}() {
               return ${field.name};
            }
            """.trimMargin()

        val setter = """

            public void set${capitalizeFirst(field.name)}(${field.type} ${field.name}) {
                this.${field.name} = ${field.name};
            }"""

        return if (field.readOnly) {
            getter
        } else {
            getter + setter
        }
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
        val extraIndent = if (prevLine != "" && prevLine.last() !in listOf(
                ';',
                '{',
                '}'
            ) && !prevLine.startsWith("//") && prevLine[0] != '@' && !prevLine.endsWith("),")
        ) {
            // This is only a heuristic, we would need something considerably smarter to be foolproof
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

data class Field(
    val name: String,
    val type: String,
    val private: Boolean,
    val readOnly: Boolean,
    val annotations: List<String>
) {
    fun getVisibility(): String {
        return if (private) {
            "private"
        } else {
            "public"
        }
    }
}
