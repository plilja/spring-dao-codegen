package se.plilja.springdaogen

import java.util.*


data class Config(val outputFolder: String, val outputPackage: String) {

    companion object {
        private val properties: Properties by lazy {
            val r = Properties()
            Config::class.java.getResource("/settings.properties").openStream().use {
                r.load(it)
            }
            r
        }

        fun readConfig(): Config {
            return Config(outputFolder(), outputPackage())
        }

        private fun outputFolder(): String {
            val f = properties.getProperty("output.folder")
            if (f.last() != '/') {
                return "$f/"
            } else {
                return f
            }
        }

        private fun outputPackage(): String {
            return properties.getProperty("output.package")
        }
    }
}