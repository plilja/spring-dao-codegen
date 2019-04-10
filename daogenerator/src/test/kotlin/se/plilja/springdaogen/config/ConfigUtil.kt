package se.plilja.springdaogen.config

import se.plilja.springdaogen.engine.model.DatabaseDialect


fun defaultTestConfig(): Config {
    return Config(
            DatabaseDialect.POSTGRES,
            "",
            "",
            "",
            "",
            "",
            "se.plilja.test",
            "",
            "",
            "",
            "se.plilja.test",
            10,
            emptyList(),
            false,
            entitySuffix = "Entity"
    )

}