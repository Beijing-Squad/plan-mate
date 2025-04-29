package data.csvDataSource.csv

import com.google.common.truth.Truth.assertThat
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
    fun `test CsvWriter throws CsvFileExceptions if file is not CSV on initialization`() {
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
    fun `test appendLine throws CsvFileExceptions if file is not CSV`() {
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
    fun `test overwriteLines throws CsvFileExceptions if file is not CSV`() {
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
    fun `test WriteHeader when file does not exist creates file with header`() {
        //Given
        mockFile.delete()

        //When
        csvWriter.writeHeader("Name, Age, Gender")

        //Then
        assertThat(mockFile.exists()).isTrue()
        assertThat(mockFile.readText()).isEqualTo("Name, Age, Gender\n")
    }

    @Test
    fun `test write header when file exists does not overwrite`() {
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
    fun `test append line when file exists appends line`() {
        //Given
        csvWriter.appendLine("John, 30, Male")

        //When && Then
        assertThat(mockFile.readText()).isEqualTo("John, 30, Male\n")
    }

    @Test
    fun `test overwrite lines overwrites content`() {
        //Given
        csvWriter.updateLines(listOf("Name, Age, Gender", "John, 30, Male"))

        //When && Then
        assertThat(mockFile.readText()).isEqualTo("Name, Age, Gender\nJohn, 30, Male\n")
    }

    @Test
    fun `test overwrite lines when list is empty creates empty file`() {
        //Given
        csvWriter.updateLines(emptyList())

        //When && Then
        assertThat(mockFile.readText()).isEqualTo("\n")
    }


    @Test
    fun `test writeHeader throws EmptyHeaderException if header is empty`() {
        //Given
        val exception = assertThrows<EmptyHeaderException> {
            csvWriter.writeHeader("")
        }

        //When && Then
        assertThat(exception.message).isEqualTo("Header cannot be empty")
    }
}