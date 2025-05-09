package data.csvDataSource.csv

import com.google.common.truth.Truth.assertThat
import data.local.csvDataSource.csv.CsvReader
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.IOException

class CsvReaderTest {

    private lateinit var tempFile:File

    @BeforeEach
    fun setup() {
        tempFile = File.createTempFile("test", ".csv")
    }

    @AfterEach
    fun cleanup() {
        tempFile.delete()
    }

    @Test
    fun `should return raw lines including header and data`() {
        //Given
        tempFile.writeText("name,age\nJohn,30\nJane,25")
        val reader = CsvReader(tempFile)

        //When
        val result = reader.readCsv()

        //Then
        assertThat(result).containsExactly("name,age", "John,30", "Jane,25")
    }

    @Test
    fun `should return single line when only header is present`() {
        //Given
        tempFile.writeText("name,age")
        val reader = CsvReader(tempFile)

        //When
        val result = reader.readCsv()

        //Then
        assertThat(result).containsExactly("name,age")
    }

    @Test
    fun `should return empty list when file is empty`() {
        //Given
        tempFile.writeText("")
        val reader = CsvReader(tempFile)

        //When
        val result = reader.readCsv()

        //Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should create new file if it does not exist`() {
        // delete the temp file to simulate "missing" file
        tempFile.delete()

        //Given
        val reader = CsvReader(tempFile)

        //When
        val result = reader.readCsv()

        // Then
        assertThat(tempFile.exists()).isTrue()
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw IOException when path is a directory`() {
        // make the path a directory
        tempFile.delete()
        tempFile.mkdir()

        //Given
        val reader = CsvReader(tempFile)

        //Then
        assertThrows<IOException> { reader.readCsv() }
    }

}