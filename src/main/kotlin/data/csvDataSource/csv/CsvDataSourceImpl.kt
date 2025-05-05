package data.csvDataSource.csv

import logic.entities.exceptions.CsvReadException
import logic.entities.exceptions.CsvWriteException
import logic.entities.exceptions.ProjectNotFoundException

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
            val allItems = loadAllDataFromFile()
            val itemId = csvDataParser.getId(item)

            val alreadyExists = allItems.any { csvDataParser.getId(it) == itemId }
            if (alreadyExists) {
                throw IllegalArgumentException("Item with ID: $itemId already exists and cannot be added again.")
            }
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
            if (id.isBlank()) {
                throw IllegalArgumentException("ID cannot be blank or null")
            }
            val allItems = loadAllDataFromFile()
            val itemToDelete = allItems.find { csvDataParser.getId(it) == id }
            if (itemToDelete == null) {
                throw ProjectNotFoundException("Item with ID: $id does not exist.")
            }
            val updatedItems = allItems.filter { csvDataParser.getId(it) != id }
            updateFile(updatedItems)
        } catch (e: Exception) {
            throw CsvWriteException("Error deleting item from CSV: ${e.message}")
        }
    }

    fun getById(id: String): T {
        return try {
            val allItems = loadAllDataFromFile()
            allItems.find { csvDataParser.getId(it) == id }
                ?: throw ProjectNotFoundException("Item with ID: $id was not found.")
        } catch (e: Exception) {
            throw CsvReadException("Error reading CSV file for item with ID: $id, reason: ${e.message}")
        }
    }
    fun updateItem(updatedItem: T) {
        try {
            val allItems = loadAllDataFromFile()
            val itemId = csvDataParser.getId(updatedItem)

            val exists = allItems.any { csvDataParser.getId(it) == itemId }
            if (!exists) {
                throw ProjectNotFoundException("Item with ID: $itemId does not exist.")
            }

            val updatedList = allItems.map {
                if (csvDataParser.getId(it) == itemId) updatedItem else it
            }

            updateFile(updatedList)
        } catch (e: Exception) {
            throw CsvWriteException("Error updating item in CSV file: ${e.message}")
        }
    }
}