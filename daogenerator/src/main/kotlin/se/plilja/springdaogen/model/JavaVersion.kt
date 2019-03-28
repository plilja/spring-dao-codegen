package se.plilja.springdaogen.model

import java.lang.IllegalArgumentException


enum class JavaVersion(val version: String) {
    Java8("1.8"),
    Java11("11");

    companion object {
        fun fromVersionNumber(version: String): JavaVersion {
            return JavaVersion.values().firstOrNull { it.version == version } ?: throw IllegalArgumentException("Supported Java versions are 1.8 and 11")
        }
    }

}