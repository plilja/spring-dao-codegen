package se.plilja.springdaogen

fun camelCase(s : String): String {
    var newWord = false
    var res = ""
    for (c in s) {
        if (c == '_') {
            newWord = true
        } else {
            res += if (newWord) {
                c.toUpperCase()
            } else {
                c.toLowerCase()
            }
            newWord = false
        }
    }
    return res
}

fun capitalizeFirst(s: String): String {
    return if (s.isEmpty()) {
        ""
    } else {
        s.get(0).toUpperCase() + s.substring(1)
    }
}
