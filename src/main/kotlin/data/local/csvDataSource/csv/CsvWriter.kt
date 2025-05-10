package data.local.csvDataSource.csv

import logic.exceptions.CsvFileExceptions
import logic.exceptions.EmptyHeaderException
import java.io.File

class CsvWriter(private val file: File) {

    init {
        if (!file.name.endsWith(".csv", ignoreCase = true)) {
            throw CsvFileExceptions("The file must have a .csv extension")
        }
    }

    fun writeHeader(header: String) {
        if (header.isBlank()) {
            throw EmptyHeaderException("Header cannot be empty")
        }

        if (!file.exists()) {
            file.writeText(header + "\n")
        }
    }

    fun appendLine(record: String) {
        file.appendText(record + "\n")
    }

    fun updateLines(lines: List<String>) {

        file.writeText(lines.joinToString("\n") + "\n")
    }
}