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
    fun setup(){
        parser = TaskCsvParser()
    }

    @Test
    fun `header returns correct CSV header`() {
        // When
        val header = parser.header()

        // Then
        assertThat(header).isEqualTo("id,projectId,title,description,createdBy,stateId,createdAt,updatedAt")
    }



    @Test
    fun `serializer returns correct CSV line`() {
        // Given
        val fixedId = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
        val task = createTask(
        ).copy(id = fixedId)

        // When
        val result = parser.serializer(task)

        // Then
        assertThat(result).isEqualTo("123e4567-e89b-12d3-a456-426614174000," +
                "defaultProjectId," +
                "defaultTitle," +
                "defaultDescription," +
                "defaultCreator," +
                "defaultStateId," +
                "2023-01-01," +
                "2023-01-01")
    }

    @Test
    fun `deserializer returns correct Audit object`() {
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
        assertThat(result.id.toString()).isEqualTo("123e4567-e89b-12d3-a456-426614174000")
        assertThat(result.projectId).isEqualTo("123e4567-e89b")
        assertThat(result.title).isEqualTo("Screen")
        assertThat(result.description).isEqualTo("View screen")
        assertThat(result.createdBy).isEqualTo("Mohamed")
        assertThat(result.stateId).isEqualTo("98")
        assertThat(result.createdAt).isEqualTo(LocalDate(2024, 4, 1))
        assertThat(result.updatedAt).isEqualTo(LocalDate(2024, 4, 1))
    }

    @Test
    fun `deserializer throws IllegalArgumentException for bad UUID`() {
        //Given
        val badLine = "not-a-uuid,Type,ID,ACTION,user,timestamp"

        //When && Then
        assertThrows<IllegalArgumentException> {
            parser.deserializer(badLine)
        }
    }

}