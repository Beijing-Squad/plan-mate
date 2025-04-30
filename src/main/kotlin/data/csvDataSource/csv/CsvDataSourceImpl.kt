package data.csvDataSource.csv

import logic.entities.exceptions.CsvReadException
import logic.entities.exceptions.CsvWriteException

class CsvDataSourceImpl<T>(
    private var reader: CsvReader,
    private var writer: CsvWriter,
    private val parser: CsvParser<T>,
) {
    fun loadAll(): List<T> {
        return try {
            val lines = reader.readCsv()
            if (lines.size <= 1) return emptyList()

            lines.drop(1).map(parser::deserializer)
        } catch (e: Exception) {
            throw CsvReadException("Error reading CSV file: ${e.message}")
        }
    }

    fun append(item: T) {
        try {
            val rawLines = reader.readCsv()
            if (rawLines.isEmpty()) {
                writer.appendLine(parser.header())
            }

            writer.appendLine(parser.serializer(item))
        } catch (e: Exception) {
            throw CsvWriteException("Error writing to CSV file: ${e.message}")
        }
    }

    fun update(items: List<T>) {
        try {
            val serializedLines = items.map(parser::serializer)
            writer.updateLines(listOf(parser.header()) + serializedLines)
        } catch (e: Exception) {
            throw CsvWriteException("Error saving to CSV file: ${e.message}")
        }
    }

    fun deleteById(id: String) {
        try {
            val allItems = loadAll()
            val updatedItems = allItems.filter { parser.getId(it) != id }
            update(updatedItems)
        } catch (e: Exception) {
            throw CsvWriteException("Error deleting item from CSV: ${e.message}")
        }
    }
}