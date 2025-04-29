package logic.useCases.audit

import com.google.common.truth.Truth.assertThat
import data.repository.AuditRepositoryImpl
import fake.createAudit
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.LocalDate
import logic.entities.ActionType
import logic.entities.EntityType
import logic.entities.UserRole
import logic.repository.AuditRepository
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi

class GetAuditLogsByTaskIdUseCaseTest {

    private lateinit var getAuditLogsByTaskIdUseCase: GetAuditLogsByTaskIdUseCase
    private lateinit var auditRepository: AuditRepository

    @BeforeEach
    fun setUp() {
        auditRepository = AuditRepositoryImpl(mockk(relaxed = true))
        getAuditLogsByTaskIdUseCase = GetAuditLogsByTaskIdUseCase(auditRepository)
    }
    @Test
    fun `should return audit logs for a valid task ID`() {
        // Given
        val taskId = "task-123"
        every { auditRepository.getAuditLogsByTaskId(taskId) } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.TASK,
                entityId = taskId,
                action = ActionType.CREATE,
                timeStamp = LocalDate(2023, 1, 1)
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = EntityType.TASK,
                entityId = taskId,
                action = ActionType.UPDATE,
                timeStamp = LocalDate(2023, 1, 2)
            )
        )

        // When
        val result = getAuditLogsByTaskIdUseCase.getAuditLogsByTaskId(taskId)

        // Then
        assertThat(result.size).isEqualTo(2)
        assertThat(result.all { it.entityId == taskId }).isTrue()
        assertThat(result.all { it.entityType == EntityType.TASK }).isTrue()
    }

    @Test
    fun `should return empty list when no audit logs exist for task ID`() {
        // Given
        val taskId = "task-456"
        every { auditRepository.getAuditLogsByTaskId(taskId) } returns emptyList()

        // When
        val result = getAuditLogsByTaskIdUseCase.getAuditLogsByTaskId(taskId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return audit logs with different action types for the same task`() {
        // Given
        val taskId = "task-789"
        every { auditRepository.getAuditLogsByTaskId(taskId) } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.TASK,
                entityId = taskId,
                action = ActionType.CREATE,
                timeStamp = LocalDate(2023, 1, 1)
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = EntityType.TASK,
                entityId = taskId,
                action = ActionType.UPDATE,
                timeStamp = LocalDate(2023, 1, 2)
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User2",
                entityType = EntityType.TASK,
                entityId = taskId,
                action = ActionType.DELETE,
                timeStamp = LocalDate(2023, 1, 3)
            )
        )

        // When
        val result = getAuditLogsByTaskIdUseCase.getAuditLogsByTaskId(taskId)

        // Then
        assertThat(result.size).isEqualTo(3)
        assertThat(result.map { it.action }).containsExactly(
            ActionType.CREATE,
            ActionType.UPDATE,
            ActionType.DELETE
        )
        assertThat(result.all { it.entityId == taskId }).isTrue()
    }


}