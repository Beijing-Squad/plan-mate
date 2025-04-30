package data.csvDataSource.csv

import logic.entities.exceptions.CsvReadException
import logic.entities.exceptions.CsvWriteException

class CsvDataSourceImpl<T>(
    private var csvFileReader: CsvReader,
    private var csvFileWriter: CsvWriter,
    private val csvDataParser: CsvParser<T>,
) {
    fun loadAllDataFromFile(): List<T> {
        return try {
            val lines = csvFileReader.readCsv()
            if (lines.size <= 1) return emptyList()

            lines.drop(1).map(csvDataParser::deserializer)
        } catch (e: Exception) {
            throw CsvReadException("Error reading CSV file: ${e.message}")
        }
    }

    fun appendToFile(item: T) {
        try {
            val rawLines = csvFileReader.readCsv()
            if (rawLines.isEmpty()) {
                csvFileWriter.appendLine(csvDataParser.header())
            }

            csvFileWriter.appendLine(csvDataParser.serializer(item))
        } catch (e: Exception) {
            throw CsvWriteException("Error writing to CSV file: ${e.message}")
        }
    }

    fun updateFile(items: List<T>) {
        try {
            val serializedLines = items.map(csvDataParser::serializer)
            csvFileWriter.updateLines(listOf(csvDataParser.header()) + serializedLines)
        } catch (e: Exception) {
            throw CsvWriteException("Error saving to CSV file: ${e.message}")
        }
    }

    fun deleteById(id: String) {
        try {
            val allItems = loadAllDataFromFile()
            val updatedItems = allItems.filter { csvDataParser.getId(it) != id }
            updateFile(updatedItems)
        } catch (e: Exception) {
            throw CsvWriteException("Error deleting item from CSV: ${e.message}")
        }
    }
}