package data.parser

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test



class StateCsvParserTest {

    private lateinit var parser: StateCsvParser

    @BeforeEach
    fun setup(){
        parser = StateCsvParser()
    }

    @Test
    fun `header returns correct CSV header`() {
        // When
        val header = parser.header()

        // Then
        assertThat(header).isEqualTo("id,name,projectId")
    }

    @Test
    fun `deserializer returns correct Audit object`() {
        // Given
        val line = "252,TODO,123e4567-e89b-12d3-a456-426614174000"

        // When
        val result = parser.deserializer(line)

        // Then
        assertThat(result.id).isEqualTo("252")
        assertThat(result.name).isEqualTo("TODO")
        assertThat(result.projectId).isEqualTo("123e4567-e89b-12d3-a456-426614174000")
    }

}