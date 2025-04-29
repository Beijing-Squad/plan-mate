package data.csvDataSource.csv

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import io.mockk.verify
import logic.entities.exceptions.CsvReadException
import logic.entities.exceptions.CsvWriteException
import org.junit.jupiter.api.assertThrows


class CsvDataSourceImplTest {

    private lateinit var reader: CsvReader
    private lateinit var writer: CsvWriter
    private lateinit var parser: CsvParser<MyData>
    private lateinit var csvDataSource: CsvDataSourceImpl<MyData>

    @BeforeEach
    fun setup() {
        reader = mockk(relaxed = true)
        writer = mockk(relaxed = true)
        parser = mockk(relaxed = true)
        csvDataSource = CsvDataSourceImpl(reader, writer, parser)
    }

    @Test
    fun `should return empty list when csv is empty`(){
        //Given
        every { reader.readCsv() } returns emptyList()

        //When
        val result = csvDataSource.loadAll()

        //Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return parsed data when csv has valid content`(){
        // Given
        val lines = listOf("Header", "John, 25, Male")
        every { reader.readCsv() } returns lines

        val parsedData = MyData("John", 25, "Male")
        every { parser.deserializer("John, 25, Male") } returns parsedData

        // When
        val result = csvDataSource.loadAll()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("John")
    }

    @Test
    fun `should write header then item when file is completely empty`() {
        // Given
        val item = MyData("Alice", 30, "Female")
        every { reader.readCsv() } returns emptyList()
        every { parser.header() } returns "Name,Age,Gender"
        every { parser.serializer(item) } returns "Alice,30,Female"

        // When
        csvDataSource.append(item)

        // Then – header, then record
        verifyOrder {
            writer.appendLine("Name,Age,Gender")
            writer.appendLine("Alice,30,Female")
        }
    }

    @Test
    fun `should append item only without writing header if file has header already`() {
        // Given
        val item = MyData("Bob", 40, "Male")
        every { reader.readCsv() } returns listOf("Name,Age,Gender")
        every { parser.serializer(item) } returns "Bob,40,Male"


        // When
        csvDataSource.append(item)

        // Then
        verify(exactly = 0) { writer.appendLine(parser.header()) }
        verify { writer.appendLine("Bob,40,Male") }
    }

    @Test
    fun `should throw CsvReadException when reader fails`() {
        // Given
        every { reader.readCsv() } throws Exception("File read error")

        // When & Then
        val exception = assertThrows<CsvReadException> {
            csvDataSource.loadAll()
        }
        assertThat(exception.message).isEqualTo("Error reading CSV file: File read error")

    }

    @Test
    fun `should append serialized item successfully`() {
        val item = MyData("John", 25, "Male")

        // Given
        every { parser.serializer(item) } returns "John, 25, Male"

        // When
        csvDataSource.append(item)

        // Then
        verify { writer.appendLine("John, 25, Male") }
    }

    @Test
    fun `should throw CsvWriteException when writer fails while appending`() {
        val item = MyData("John", 25, "Male")

        // Given
        every { parser.serializer(item) } returns "John, 25, Male"

        every { writer.appendLine(any()) } throws Exception("Write error")

        // When & Then
        val exception = assertThrows<CsvWriteException> {
            csvDataSource.append(item)
        }

        assertThat(exception.message).isEqualTo("Error writing to CSV file: Write error")
    }

    @Test
    fun `should save all items with header and serialized lines`() {
        val items = listOf(MyData("John", 25, "Male"), MyData("Jane", 30, "Female"))

        // Given
        every { parser.serializer(any()) } returns "Serialized Data"
        every { parser.header() } returns "Name, Age, Gender"

        // When
        csvDataSource.update(items)

        // Then
        verify { writer.updateLines(listOf("Name, Age, Gender", "Serialized Data", "Serialized Data")) }
    }

    @Test
    fun `should throw CsvWriteException when writer fails while saving all`() {
        val items = listOf(MyData("John", 25, "Male"))
        // Given
        every { parser.serializer(any()) } returns "Serialized Data"
        every { parser.header() } returns "Name, Age, Gender"

        every { writer.updateLines(any()) } throws Exception("Save error")

        // When & Then
        val exception = assertThrows<CsvWriteException> {
            csvDataSource.update(items)
        }

        assertThat(exception.message).isEqualTo("Error saving to CSV file: Save error")
    }

}

// Sample data class for testing purposes
data class MyData(val name: String, val age: Int, val gender: String)