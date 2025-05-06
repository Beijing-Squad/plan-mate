package data.parser

import com.google.common.truth.Truth.assertThat
import fake.createAudit
import kotlinx.datetime.LocalDateTime
import logic.entities.ActionType
import logic.entities.EntityType
import logic.entities.UserRole
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class AuditCsvParserTest {
    private lateinit var parser: AuditCsvParser

    @BeforeEach
    fun setup() {
        parser = AuditCsvParser()
    }

    @Test
    fun `should return correct CSV header`() {
        // When
        val header = parser.header()

        // Then
        assertThat(header).isEqualTo("id,userRole,userName,entityId,entityType,action,oldState,newState,timeStamp")
    }

    @Test
    fun `should serialize Audit object into correct CSV line`() {
        // Given
        val fixedId = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
        val fixedTime = LocalDateTime.parse("2024-04-01T00:00")
        val audit = createAudit(
            userRole = UserRole.ADMIN,
            userName = "Ahmed",
            entityId = "task-1",
            entityType = EntityType.TASK,
            action = ActionType.CREATE,
            timeStamp = fixedTime
        ).copy(id = fixedId)

        // When
        val result = parser.serializer(audit)

        // Then
        assertThat(result).isEqualTo("123e4567-e89b-12d3-a456-426614174000,ADMIN,Ahmed,task-1,TASK,CREATE,,new,2024-04-01")
    }

    @Test
    fun `should deserialize correct CSV line into Audit object`() {
        // Given
        val line = "123e4567-e89b-12d3-a456-426614174000,ADMIN,Ahmed,task-95,TASK,CREATE,,new,2024-04-01T00:00"

        // When
        val result = parser.deserializer(line)

        // Then
        with(result) {
            assertThat(id.toString()).isEqualTo("123e4567-e89b-12d3-a456-426614174000")
            assertThat(userRole).isEqualTo(UserRole.ADMIN)
            assertThat(userName).isEqualTo("Ahmed")
            assertThat(entityId).isEqualTo("task-95")
            assertThat(entityType).isEqualTo(EntityType.TASK)
            assertThat(action).isEqualTo(ActionType.CREATE)
            assertThat(timeStamp).isEqualTo(LocalDateTime(2024, 4, 1, 0, 0))
        }
    }

    @Test
    fun `should throw IllegalArgumentException when UUID is invalid`() {
        //Given
        val badLine = "not-a-uuid,Type,ID,ACTION,[user],timestamp"

        //When && Then
        assertThrows<IllegalArgumentException> {
            parser.deserializer(badLine)
        }
    }

    @Test
    fun `should return correct id from Audit object`() {
        // Given
        val fakeId = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
        val audit = createAudit().copy(id = fakeId)

        // When
        val result = parser.getId(audit)

        // Then
        assertThat(result).isEqualTo("123e4567-e89b-12d3-a456-426614174000")
    }

}