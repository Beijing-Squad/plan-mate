package data.parser

import com.google.common.truth.Truth.assertThat
import data.local.csvDataSource.parser.ProjectCsvParser
import fake.createProject
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProjectCsvParserTest {

    private lateinit var parser: ProjectCsvParser

    @BeforeEach
    fun setup() {
        parser = ProjectCsvParser()
    }

    @Test
    fun `should return correct CSV header`() {
        // When
        val header = parser.header()

        // Then
        assertThat(header).isEqualTo("id,name,description,createdBy,createdAt,updatedAt")
    }


    @Test
    fun `should serialize project to correct CSV line`() {
        // Given
        val fixedId = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
        val project = createProject(
        ).copy(id = fixedId)

        // When
        val result = parser.serializer(project)

        // Then
        with(result) {
            assertThat(this).isEqualTo(
                "123e4567-e89b-12d3-a456-426614174000," + "defaultProjectName," + "defaultDescription," + "defaultCreator," + "2023-01-01," + "2023-01-01"
            )
        }

    }

    @Test
    fun `should deserialize line to correct Project object`() {
        // Given
        val line = "123e4567-e89b-12d3-a456-426614174000,Mobile,Application,Mada,2024-04-01,2024-04-01"

        // When
        val result = parser.deserializer(line)

        // Then
        with(result) {
            assertThat(id.toString()).isEqualTo("123e4567-e89b-12d3-a456-426614174000")
            assertThat(name).isEqualTo("Mobile")
            assertThat(description).isEqualTo("Application")
            assertThat(createdBy).isEqualTo("Mada")
            assertThat(createdAt).isEqualTo(LocalDate(2024, 4, 1))
            assertThat(updatedAt).isEqualTo(LocalDate(2024, 4, 1))
        }
    }

    @Test
    fun `should throw IllegalArgumentException when UUID is invalid`() {
        //Given
        val badLine = "not-a-uuid,Type,ID,ACTION,user,timestamp"

        //When && Then
        assertThrows<IllegalArgumentException> {
            parser.deserializer(badLine)
        }
    }

    @Test
    fun `should return correct id from Audit object`() {
        // Given
        val fakeId = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
        val project = createProject().copy(id = fakeId)

        // When
        val result = parser.getId(project)

        // Then
        assertThat(result).isEqualTo("123e4567-e89b-12d3-a456-426614174000")
    }

}