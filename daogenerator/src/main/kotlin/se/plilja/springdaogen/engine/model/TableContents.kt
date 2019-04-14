package se.plilja.springdaogen.engine.model


interface TableContents {
    fun getContents(table: Table): List<HashMap<String, Any>>
}