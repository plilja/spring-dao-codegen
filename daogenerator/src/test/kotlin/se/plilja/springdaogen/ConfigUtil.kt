package se.plilja.springdaogen

import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.DatabaseDialect


fun defaultTestConfig(): Config {
    return Config(
            "",
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