package se.plilja.springdaogen.codegeneration

import java.util.*


abstract class ClassFileGenerator(
    val name: String,
    val sourceBaseFolder: String,
    val packageName: String
) {

    private val imports = TreeSet<String>()

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

    fun getOutputFolder(): String {
        return "${sourceBaseFolder}${packageName.replace(".", "/")}"
    }

    fun getOutputFileName(): String {
        return "${getOutputFolder()}/${name}.java"
    }

    abstract fun generate(): String

    fun formatImports() = imports.map { "import $it;" }.joinToString("\n")

    fun formatCode(code: String) = rightTrimLines(indent(code))

    private fun indent(code: String): String {
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
    }

    private fun rightTrimLines(s: String): String {
        return s.split("\n").map { it.trimEnd() }.joinToString("\n")
    }
}