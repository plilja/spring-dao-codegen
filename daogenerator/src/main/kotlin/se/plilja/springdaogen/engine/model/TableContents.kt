package se.plilja.springdaogen.engine.model

import se.plilja.springdaogen.config.Config


interface TableContents {
    fun getContents(config: Config, table: Table): List<HashMap<String, Any>>
}