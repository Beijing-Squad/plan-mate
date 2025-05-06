package data.parser

import com.google.common.truth.Truth.assertThat
import fake.createState
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class StateCsvParserTest {

    private lateinit var parser: TaskStateCsvParser

    @BeforeEach
    fun setup() {
        parser = TaskStateCsvParser()
    }

    @Test
    fun `should return correct CSV header`() {
        // When
        val header = parser.header()

        // Then
        assertThat(header).isEqualTo("id,name,projectId")
    }

    @Test
    fun `should deserialize line to correct State object`() {
        // Given
        val line = "252,TODO,123e4567-e89b-12d3-a456-426614174000"

        // When
        val result = parser.deserializer(line)

        // Then
        with(result) {
            assertThat(id).isEqualTo("252")
            assertThat(name).isEqualTo("TODO")
            assertThat(projectId).isEqualTo("123e4567-e89b-12d3-a456-426614174000")
        }
    }

    @Test
    fun `should return correct id from Audit object`() {
        // Given
        val fakeId = "123e4567-e89b-12d3-a456-426614174000"
        val state = createState().copy(id = fakeId)

        // When
        val result = parser.getId(state)

        // Then
        assertThat(result).isEqualTo("123e4567-e89b-12d3-a456-426614174000")
    }

}