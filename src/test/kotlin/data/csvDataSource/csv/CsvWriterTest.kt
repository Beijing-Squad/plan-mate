package data.csvDataSource.csv

import com.google.common.truth.Truth.assertThat
import data.local.csvDataSource.csv.CsvWriter
import logic.entities.exceptions.CsvFileExceptions
import logic.entities.exceptions.EmptyHeaderException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

class CsvWriterTest {

    private lateinit var mockFile: File
    private lateinit var csvWriter: CsvWriter

    @BeforeEach
    fun setup() {
        mockFile = File.createTempFile("test", ".csv")
        csvWriter = CsvWriter(mockFile)
    }


    @AfterEach
    fun cleanup() {

        mockFile.delete()
    }

    @Test
    fun `should throw CsvFileExceptions if file is not CSV on initialization`() {
        //Given
        val nonCsvFile = File("test.txt")

        //When
        val exception = assertThrows<CsvFileExceptions> {
            CsvWriter(nonCsvFile)
        }

        //Then
        assertThat(exception.message).isEqualTo("The file must have a .csv extension")
    }

    @Test
    fun `should throw CsvFileExceptions when appending line if file is not CSV`() {
        //Given
        val nonCsvFile = File("test.txt")

        //When
        val exception = assertThrows<CsvFileExceptions> {
            CsvWriter(nonCsvFile)
            CsvWriter(nonCsvFile).appendLine("Some record")
        }

        //Then
        assertThat(exception.message).isEqualTo("The file must have a .csv extension")
    }

    @Test
    fun `should throw CsvFileExceptions when overwriting lines if file is not CSV`() {
        //Given
        val nonCsvFile = File("test.txt")

        //When
        val exception = assertThrows<CsvFileExceptions> {
            CsvWriter(nonCsvFile)
            CsvWriter(nonCsvFile).updateLines(listOf("Header", "Row 1", "Row 2"))
        }

        //Then
        assertThat(exception.message).isEqualTo("The file must have a .csv extension")
    }

    @Test
    fun `should create file with header when file does not exist`() {
        //Given
        mockFile.delete()

        //When
        csvWriter.writeHeader("Name, Age, Gender")

        //Then
        assertThat(mockFile.exists()).isTrue()
        assertThat(mockFile.readText()).isEqualTo("Name, Age, Gender\n")
    }

    @Test
    fun `should not overwrite header when file already exists`() {
        //Given
        val existingHeader = "Name, Age, Gender\n"
        mockFile.writeText(existingHeader)

        //When
        csvWriter.writeHeader("Name, Age, Gender")

        //Then
        assertThat(mockFile.exists()).isTrue()
        assertThat(mockFile.readText()).isEqualTo(existingHeader)
    }

    @Test
    fun `should append line to file when file exists`() {
        //Given
        csvWriter.appendLine("John, 30, Male")

        //When && Then
        assertThat(mockFile.readText()).isEqualTo("John, 30, Male\n")
    }

    @Test
    fun `should overwrite lines in file with provided content`() {
        //Given
        csvWriter.updateLines(listOf("Name, Age, Gender", "John, 30, Male"))

        //When && Then
        assertThat(mockFile.readText()).isEqualTo("Name, Age, Gender\nJohn, 30, Male\n")
    }

    @Test
    fun `should create empty file when overwriting with empty list`() {
        //Given
        csvWriter.updateLines(emptyList())

        //When && Then
        assertThat(mockFile.readText()).isEqualTo("\n")
    }


    @Test
    fun `tshould throw EmptyHeaderException when header is empty`() {
        //Given
        val exception = assertThrows<EmptyHeaderException> {
            csvWriter.writeHeader("")
        }

        //When && Then
        assertThat(exception.message).isEqualTo("Header cannot be empty")
    }
}