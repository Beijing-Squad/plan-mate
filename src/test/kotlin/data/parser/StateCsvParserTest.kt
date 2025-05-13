package data.parser

import com.google.common.truth.Truth.assertThat
import data.local.csvDataSource.parser.TaskStateCsvParser
import fake.createState
import logic.entity.TaskState
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class StateCsvParserTest {

    private lateinit var parser: TaskStateCsvParser

    @BeforeEach
    fun setup() {
        parser = TaskStateCsvParser()
    }

    @Test
    fun `should return correct CSV header`() {
        val header = parser.header()
        assertThat(header).isEqualTo("id,name,projectId")
    }

    @Test
    fun `should deserialize line to correct State object`() {
        // Given
        val id = "123e4567-e89b-12d3-a456-426614174000"
        val name = "TODO"
        val projectId = "project-001"
        val line = "$id,$name,$projectId"

        // When
        val result: TaskState = parser.deserializer(line)

        // Then
        with(result) {
            assertThat(this.id).isEqualTo(Uuid.parse(id))
            assertThat(this.name).isEqualTo(name)
            assertThat(this.projectId).isEqualTo(projectId)
        }
    }

    @Test
    fun `should return correct id from State object`() {
        // Given
        val fixedId = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
        val state = createState(id = fixedId)

        // When
        val result = parser.getId(state)

        // Then
        assertThat(result).isEqualTo(fixedId.toString())
    }

    @Test
    fun `should serialize TaskState correctly`() {
        // Given
        val state = createState(
            id = Uuid.parse("11111111-1111-1111-1111-111111111111"),
            name = "InProgress",
            projectId = Uuid.parse("21111111-1111-1111-1111-111111111111")
        )

        // When
        val csv = parser.serializer(state)

        // Then
        assertThat(csv).isEqualTo(
            "11111111-1111-1111-1111-111111111111,InProgress,21111111-1111-1111-1111-111111111111"
        )
    }

    @Test
    fun `serialize and deserialize should result in the same TaskState`() {
        // Given
        val original = createState(
            id = Uuid.parse("22222222-2222-2222-2222-222222222222"),
            name = "Review",
            projectId = Uuid.parse("21111111-1111-1111-1111-111111111111")
        )

        // When
        val serialized = parser.serializer(original)
        val deserialized = parser.deserializer(serialized)

        // Then
        assertThat(deserialized).isEqualTo(original)
    }
}
