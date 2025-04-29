package data.parser

import com.google.common.truth.Truth.assertThat
import fake.createTask
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TaskCsvParserTest {

    private lateinit var parser: TaskCsvParser

    @BeforeEach
    fun setup() {
        parser = TaskCsvParser()
    }

    @Test
    fun `should return correct CSV header`() {
        // When
        val header = parser.header()

        // Then
        with(header) {
            assertThat(this).isEqualTo("id,projectId,title,description,createdBy,stateId,createdAt,updatedAt")
        }
    }

    @Test
    fun `should serialize task to correct CSV line`() {
        // Given
        val fixedId = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
        val task = createTask().copy(id = fixedId)

        // When
        val result = parser.serializer(task)

        // Then
        with(result) {
            assertThat(this).isEqualTo("123e4567-e89b-12d3-a456-426614174000," +
                    "defaultProjectId," +
                    "defaultTitle," +
                    "defaultDescription," +
                    "defaultCreator," +
                    "defaultStateId," +
                    "2023-01-01," +
                    "2023-01-01")
        }
    }

    @Test
    fun `should deserialize line to correct Task object`() {
        // Given
        val line = "123e4567-e89b-12d3-a456-426614174000," +
                "123e4567-e89b," +
                "Screen," +
                "View screen," +
                "Mohamed," +
                "98," +
                "2024-04-01," +
                "2024-04-01"

        // When
        val result = parser.deserializer(line)

        // Then
        with(result) {
            assertThat(id.toString()).isEqualTo("123e4567-e89b-12d3-a456-426614174000")
            assertThat(projectId).isEqualTo("123e4567-e89b")
            assertThat(title).isEqualTo("Screen")
            assertThat(description).isEqualTo("View screen")
            assertThat(createdBy).isEqualTo("Mohamed")
            assertThat(stateId).isEqualTo("98")
            assertThat(createdAt).isEqualTo(LocalDate(2024, 4, 1))
            assertThat(updatedAt).isEqualTo(LocalDate(2024, 4, 1))
        }
    }

    @Test
    fun `should throw IllegalArgumentException for bad UUID in deserialization`() {
        // Given
        val badLine = "not-a-uuid,Type,ID,ACTION,user,timestamp"

        // When && Then
        assertThrows<IllegalArgumentException> {
            parser.deserializer(badLine)
        }
    }
}
