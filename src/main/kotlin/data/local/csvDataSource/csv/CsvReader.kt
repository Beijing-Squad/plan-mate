package data.local.csvDataSource.csv

import java.io.File

class CsvReader(private val file: File) {

    fun readCsv(): List<String> {
        if (!file.exists()) file.createNewFile()
        return file.readLines()
    }

}