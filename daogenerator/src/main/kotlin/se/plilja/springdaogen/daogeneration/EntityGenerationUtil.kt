package se.plilja.springdaogen.daogeneration

import se.plilja.springdaogen.classgenerators.AbstractClassGenerator
import se.plilja.springdaogen.model.Config


fun ensureImported(g: AbstractClassGenerator, config: Config, f: () -> Pair<String, String>) {
    if (config.frameworkOutputPackage != config.entityOutputPackage) {
        g.addImport("${config.frameworkOutputPackage}.${f().first}")
    }
}
