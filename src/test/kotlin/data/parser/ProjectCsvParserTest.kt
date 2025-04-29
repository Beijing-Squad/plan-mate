package data.parser

import com.google.common.truth.Truth.assertThat
import fake.createAudit
import fake.createProject
import kotlinx.datetime.LocalDate
import logic.entities.ActionType
import logic.entities.EntityType
import logic.entities.UserRole
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProjectCsvParserTest{

    private lateinit var parser: ProjectCsvParser

    @BeforeEach
    fun setup(){
        parser = ProjectCsvParser()
    }

    @Test
    fun `header returns correct CSV header`() {
        // When
        val header = parser.header()

        // Then
        assertThat(header).isEqualTo("id,name,description,createdBy,createdAt,updatedAt")
    }


    @Test
    fun `serializer returns correct CSV line`() {
        // Given
        val fixedId = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
        val project = createProject(
        ).copy(id = fixedId)

        // When
        val result = parser.serializer(project)

        // Then
        assertThat(result).isEqualTo("123e4567-e89b-12d3-a456-426614174000," +
                "defaultProjectName," +
                "defaultDescription," +
                "defaultCreator," +
                "2023-01-01," +
                "2023-01-01")
    }

    @Test
    fun `deserializer returns correct Audit object`() {
        // Given
        val line = "123e4567-e89b-12d3-a456-426614174000,Mobile,Application,Mada,2024-04-01,2024-04-01"

        // When
        val result = parser.deserializer(line)

        // Then
        assertThat(result.id.toString()).isEqualTo("123e4567-e89b-12d3-a456-426614174000")
        assertThat(result.name).isEqualTo("Mobile")
        assertThat(result.description).isEqualTo("Application")
        assertThat(result.createdBy).isEqualTo("Mada")
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