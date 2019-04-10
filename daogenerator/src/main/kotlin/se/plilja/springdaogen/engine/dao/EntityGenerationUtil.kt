package se.plilja.springdaogen.engine.dao

import se.plilja.springdaogen.config.Config
import se.plilja.springdaogen.syntaxgenerator.AbstractClassGenerator


fun ensureImported(g: AbstractClassGenerator, config: Config, f: () -> Pair<String, String>) {
    if (config.frameworkOutputPackage != config.entityOutputPackage) {
        g.addImport("${config.frameworkOutputPackage}.${f().first}")
    }
}
