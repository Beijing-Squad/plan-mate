package data.csvDataSource.csv

interface CsvParser<T> {

    fun header(): String
    fun serializer(item: T): String
    fun deserializer(content: String): T
    fun getId(item: T): String
}
