package logic.useCases.audit

import com.google.common.truth.Truth.assertThat
import fake.createAudit
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.ActionType
import logic.entities.EntityType
import logic.entities.type.UserRole
import logic.repository.AuditRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllAuditLogsUseCaseTest {

    private lateinit var getAllAuditLogsUseCase: GetAllAuditLogsUseCase
    private lateinit var auditRepository: AuditRepository

    @BeforeEach
    fun setUp() {
        auditRepository = mockk(relaxed = true)
        getAllAuditLogsUseCase = GetAllAuditLogsUseCase(auditRepository)
    }

    @Test
    fun `should return all audit logs when called`() = runTest {
        // Given
        coEvery { auditRepository.getAllAuditLogs() } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.PROJECT,
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "MATE",
                entityType = EntityType.TASK,
            )
        )

        // When
        val result = getAllAuditLogsUseCase.getAllAuditLogs()

        // Then
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `should return empty list when audit repo is empty`() = runTest {
        // Given
        coEvery { auditRepository.getAllAuditLogs() } returns emptyList()

        // When
        val result = getAllAuditLogsUseCase.getAllAuditLogs()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return audit logs when different action types is provided`() = runTest {
        // Given
        coEvery { auditRepository.getAllAuditLogs() } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.PROJECT,
                action = ActionType.CREATE,
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = EntityType.TASK,
                action = ActionType.UPDATE,
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User2",
                entityType = EntityType.PROJECT,
                action = ActionType.DELETE,
            )
        )

        // When
        val result = getAllAuditLogsUseCase.getAllAuditLogs()

        // Then
        assertThat(result.size).isEqualTo(3)
        assertThat(result.map { it.action }).containsExactly(
            ActionType.CREATE,
            ActionType.UPDATE,
            ActionType.DELETE
        )
    }

    @Test
    fun `should return audit logs for both project and task entities when they exist`() = runTest {
        // Given
        coEvery { auditRepository.getAllAuditLogs() } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.PROJECT,
                entityId = "project-123",
                action = ActionType.CREATE,
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = EntityType.TASK,
                entityId = "task-456",
                action = ActionType.CREATE,
            )
        )

        // When
        val result = getAllAuditLogsUseCase.getAllAuditLogs()

        // Then
        assertThat(result.size).isEqualTo(2)
        assertThat(result.map { it.entityType }).containsExactly(
            EntityType.PROJECT,
            EntityType.TASK
        )
        assertThat(result.map { it.entityId }).containsExactly(
            "project-123",
            "task-456"
        )
    }
}
