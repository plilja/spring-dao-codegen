package se.plilja.springdaogen.engine.dao

import se.plilja.springdaogen.classgenerators.AbstractClassGenerator
import se.plilja.springdaogen.config.Config


fun ensureImported(g: AbstractClassGenerator, config: Config, f: () -> Pair<String, String>) {
    if (config.frameworkOutputPackage != config.entityOutputPackage) {
        g.addImport("${config.frameworkOutputPackage}.${f().first}")
    }
}
