package data.parser

import com.google.common.truth.Truth.assertThat
import fake.createUser
import logic.entities.UserRole
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserCsvParserTest {

    private lateinit var parser: UserCsvParser

    @BeforeEach
    fun setup(){
        parser = UserCsvParser()
    }

    @Test
    fun `header returns correct CSV header`() {
        // When
        val header = parser.header()

        // Then
        assertThat(header).isEqualTo("id,userName,password,role")
    }

    @Test
    fun `serializer returns correct CSV line`() {
        // Given
        val fixedId = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
        val task = createUser(
        ).copy(id = fixedId)

        // When
        val result = parser.serializer(task)

        // Then
        assertThat(result).isEqualTo("123e4567-e89b-12d3-a456-426614174000," +
                "defaultUser," +
                "defaultPassword," +
                "MATE")
    }

    @Test
    fun `deserializer returns correct Audit object`() {
        // Given
        val line = "123e4567-e89b-12d3-a456-426614174000," +
                   "Youssef," +
                   "Youssef10125," +
                   "MATE,"


        // When
        val result = parser.deserializer(line)

        // Then
        assertThat(result.id.toString()).isEqualTo("123e4567-e89b-12d3-a456-426614174000")
        assertThat(result.userName).isEqualTo("Youssef")
        assertThat(result.password).isEqualTo("Youssef10125")
        assertThat(result.role).isEqualTo(UserRole.MATE)
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