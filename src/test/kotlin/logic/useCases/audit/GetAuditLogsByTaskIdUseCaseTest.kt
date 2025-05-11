package logic.useCases.audit

import com.google.common.truth.Truth.assertThat
import fake.createAudit
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.ActionType
import logic.entities.EntityType
import logic.entities.type.UserRole
import logic.exceptions.InvalidInputException
import logic.repository.AuditRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetAuditLogsByTaskIdUseCaseTest {

    private lateinit var getAuditLogsByTaskIdUseCase: GetAuditLogsByTaskIdUseCase
    private lateinit var auditRepository: AuditRepository

    @BeforeEach
    fun setUp() {
        auditRepository = mockk(relaxed = true)
        getAuditLogsByTaskIdUseCase = GetAuditLogsByTaskIdUseCase(auditRepository)
    }

    @Test
    fun `should return audit logs when a valid task ID is provided`() = runTest {
        val taskId = "task-123"
        coEvery { auditRepository.getAuditLogsByTaskId(taskId) } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.TASK,
                entityId = taskId,
                action = ActionType.CREATE,
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = EntityType.TASK,
                entityId = taskId,
                action = ActionType.UPDATE,
            )
        )

        val result = getAuditLogsByTaskIdUseCase.getAuditLogsByTaskId(taskId)

        assertThat(result.size).isEqualTo(2)
        assertThat(result.all { it.entityId == taskId }).isTrue()
        assertThat(result.all { it.entityType == EntityType.TASK }).isTrue()
    }

    @Test
    fun `should return empty list when no audit logs exist for task ID`() = runTest {
        val taskId = "task-456"
        coEvery { auditRepository.getAuditLogsByTaskId(taskId) } returns emptyList()

        val result = getAuditLogsByTaskIdUseCase.getAuditLogsByTaskId(taskId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return audit logs with different action types for the same task when provided`() = runTest {
        val taskId = "task-789"
        coEvery { auditRepository.getAuditLogsByTaskId(taskId) } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.TASK,
                entityId = taskId,
                action = ActionType.CREATE,
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = EntityType.TASK,
                entityId = taskId,
                action = ActionType.UPDATE,
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User2",
                entityType = EntityType.TASK,
                entityId = taskId,
                action = ActionType.DELETE,
            )
        )

        val result = getAuditLogsByTaskIdUseCase.getAuditLogsByTaskId(taskId)

        assertThat(result.size).isEqualTo(3)
        assertThat(result.map { it.action }).containsExactly(
            ActionType.CREATE,
            ActionType.UPDATE,
            ActionType.DELETE
        )
        assertThat(result.all { it.entityId == taskId }).isTrue()
    }

    @Test
    fun `should return only audit logs for the specified task ID when provided`() = runTest {
        val taskId = "task-123"
        val otherTaskId = "task-456"
        coEvery { auditRepository.getAuditLogsByTaskId(taskId) } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.TASK,
                entityId = taskId,
                action = ActionType.CREATE,
            )
        )
        coEvery { auditRepository.getAuditLogsByTaskId(otherTaskId) } returns listOf(
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = EntityType.TASK,
                entityId = otherTaskId,
                action = ActionType.CREATE,
            )
        )

        val result = getAuditLogsByTaskIdUseCase.getAuditLogsByTaskId(taskId)

        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].entityId).isEqualTo(taskId)
        assertThat(result[0].entityType).isEqualTo(EntityType.TASK)
    }

    @Test
    fun `should return empty list when task ID is invalid`() = runTest {
        val invalidTaskId = "invalid-task-999"
        coEvery { auditRepository.getAuditLogsByTaskId(invalidTaskId) } returns emptyList()

        val result = getAuditLogsByTaskIdUseCase.getAuditLogsByTaskId(invalidTaskId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw InvalidInputException when task ID is blank`() = runTest {
        val exception = assertThrows<InvalidInputException> {
            getAuditLogsByTaskIdUseCase.getAuditLogsByTaskId("")
        }
        assertThat(exception.message).isEqualTo("Error: ID shouldn't be blank")
    }
}
