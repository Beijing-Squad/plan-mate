package data.parser

import com.google.common.truth.Truth.assertThat
import fake.createAudit
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
class AuditCsvParserTest {
    private lateinit var parser: AuditCsvParser

    @BeforeEach
    fun setup() {
        parser = AuditCsvParser()
    }

    @Test
    fun `header returns correct CSV header`() {
        // When
        val header = parser.header()

        // Then
        assertThat(header).isEqualTo("id,userRole,userName,entityId,entityType,action,oldState,newState,timeStamp")
    }

    @Test
    fun `serializer returns correct CSV line`() {
        // Given
        val fixedId = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
        val audit = createAudit(
            userRole = UserRole.ADMIN,
            userName = "Ahmed",
            entityId = "task-1",
            entityType = EntityType.TASK,
            action = ActionType.CREATE,
            oldState = null,
            newState = "new",
            timeStamp = LocalDate.parse("2024-04-01")
        ).copy(id = fixedId)

        // When
        val result = parser.serializer(audit)

        // Then
        assertThat(result).isEqualTo("123e4567-e89b-12d3-a456-426614174000,ADMIN,Ahmed,task-1,TASK,CREATE,,new,2024-04-01")
    }

    @Test
    fun `deserializer returns correct Audit object`() {
        // Given
        val line = "123e4567-e89b-12d3-a456-426614174000,ADMIN,Ahmed,task-95,TASK,CREATE,,new,2024-04-01"

        // When
        val result = parser.deserializer(line)

        // Then
        assertThat(result.id.toString()).isEqualTo("123e4567-e89b-12d3-a456-426614174000")
        assertThat(result.userRole).isEqualTo(UserRole.ADMIN)
        assertThat(result.userName).isEqualTo("Ahmed")
        assertThat(result.entityId).isEqualTo("task-95")
        assertThat(result.entityType).isEqualTo(EntityType.TASK)
        assertThat(result.action).isEqualTo(ActionType.CREATE)
        assertThat(result.oldState).isNull()
        assertThat(result.newState).isEqualTo("new")
        assertThat(result.timeStamp).isEqualTo(LocalDate(2024, 4, 1))
    }

    @Test
    fun `deserializer throws IllegalArgumentException for bad UUID`() {
        //Given
        val badLine = "not-a-uuid,Type,ID,ACTION,[user],timestamp"

        //When && Then
        assertThrows<IllegalArgumentException> {
            parser.deserializer(badLine)
        }
    }

}