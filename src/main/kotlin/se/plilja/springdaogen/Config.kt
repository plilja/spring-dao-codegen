package se.plilja.springdaogen

import java.util.*


class Config {
    companion object {
        private val properties: Properties by lazy {
            val r = Properties()
            Config::class.java.getResource("/settings.properties").openStream().use {
                r.load(it)
            }
            r
        }

        fun outputFolder(): String {
            val f = properties.getProperty("output.folder")
            if (f.last() != '/') {
                return "$f/"
            } else {
                return f
            }
        }
    }
}