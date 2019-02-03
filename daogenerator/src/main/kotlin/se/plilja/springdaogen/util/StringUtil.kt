package se.plilja.springdaogen.util


fun camelCase(s: String): String {
    return if (isAllUpper(s)) {
        s.toLowerCase()
    } else if (!s.contains("_")) {
        // Looks like it's already camel
        s
    } else {
        lowerCaseFirst(
            s.split("_").map { capitalizeFirst(it.toLowerCase()) }.joinToString(
                ""
            )
        )
    }
}

private fun isAllUpper(s: String) = s.matches(Regex("[A-Z0-9]*"))

private fun isAllLower(s: String) = s.matches(Regex("[a-z0-9]*"))

fun snakeCase(s: String): String {
    if (s.contains("_")) {
        // Looks like it's already snake case
        return s
    } else if (isAllLower(s) or isAllUpper(s)) {
        return s.toLowerCase()
    } else {
        val parts = ArrayList<String>()
        var part = ""
        for (c in s) {
            if (c.isUpperCase()) {
                if (!part.isEmpty()) {
                    parts.add(part)
                }
                part = "$c"
            } else {
                part += c
            }
        }
        if (!part.isEmpty()) {
            parts.add(part)
        }
        return parts.map { it.toLowerCase() }.joinToString("_")
    }
}


fun capitalizeFirst(s: String): String {
    return if (s.isEmpty()) {
        ""
    } else {
        s[0].toUpperCase() + s.substring(1)
    }
}

fun capitalizeLast(s: String): String {
    return capitalizeFirst(s.reversed()).reversed()
}

fun lowerCaseFirst(s: String): String {
    return if (s.isEmpty()) {
        ""
    } else {
        s[0].toLowerCase() + s.substring(1)
    }
}
