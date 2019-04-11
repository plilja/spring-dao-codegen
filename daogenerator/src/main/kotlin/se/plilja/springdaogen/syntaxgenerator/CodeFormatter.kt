package se.plilja.springdaogen.syntaxgenerator


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
