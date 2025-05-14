package data.parser

import com.google.common.truth.Truth.assertThat
import data.local.csvDataSource.parser.UserCsvParser
import fake.createUser
import logic.entity.type.UserRole
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserCsvParserTest {

    private lateinit var parser: UserCsvParser

    @BeforeEach
    fun setup() {
        parser = UserCsvParser()
    }

    @Test
    fun `should return correct CSV header`() {
        // When
        val header = parser.header()

        // Then
        with(header) {
            assertThat(this).isEqualTo("id,userName,password,role")
        }
    }

    @Test
    fun `should serialize user to correct CSV line`() {
        // Given
        val fixedId = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
        val user = createUser().copy(id = fixedId)

        // When
        val result = parser.serializer(user)

        // Then
        with(result) {
            assertThat(this).isEqualTo(
                "123e4567-e89b-12d3-a456-426614174000," + "defaultUser," + "defaultPassword," + "MATE"
            )
        }
    }

    @Test
    fun `should deserialize line to correct User object`() {
        // Given
        val line = "123e4567-e89b-12d3-a456-426614174000," + "Youssef," + "Youssef10125," + "MATE"

        // When
        val result = parser.deserializer(line)

        // Then
        with(result) {
            assertThat(id.toString()).isEqualTo("123e4567-e89b-12d3-a456-426614174000")
            assertThat(userName).isEqualTo("Youssef")
            assertThat(password).isEqualTo("Youssef10125")
            assertThat(role).isEqualTo(UserRole.MATE)
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

    @Test
    fun `should return correct id from Audit object`() {
        // Given
        val fakeId = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
        val user = createUser().copy(id = fakeId)

        // When
        val result = parser.getId(user)

        // Then
        assertThat(result).isEqualTo("123e4567-e89b-12d3-a456-426614174000")
    }
}