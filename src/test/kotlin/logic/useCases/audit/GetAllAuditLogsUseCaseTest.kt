package logic.useCases.audit

import com.google.common.truth.Truth.assertThat
import fake.createAudit
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entity.Audit
import logic.entity.type.UserRole
import logic.repository.AuditRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi

class GetAllAuditLogsUseCaseTest {

    private lateinit var getAllAuditLogsUseCase: GetAllAuditLogsUseCase
    private lateinit var auditRepository: AuditRepository

    @BeforeEach
    fun setUp() {
        auditRepository = mockk(relaxed = true)
        getAllAuditLogsUseCase = GetAllAuditLogsUseCase(auditRepository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return all audit logs when called`() = runTest {
        // Given
        coEvery { auditRepository.getAllAuditLogs() } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = Audit.EntityType.PROJECT,
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "MATE",
                entityType = Audit.EntityType.TASK,
            )
        )

        // When
        val result = getAllAuditLogsUseCase.getAllAuditLogs()

        // Then
        assertThat(result.size).isEqualTo(2)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return empty list when audit repo is empty`() = runTest {
        // Given
        coEvery { auditRepository.getAllAuditLogs() } returns emptyList()

        // When
        val result = getAllAuditLogsUseCase.getAllAuditLogs()

        // Then
        assertThat(result).isEmpty()
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return audit logs when different action types is provided`() = runTest {
        // Given
        coEvery { auditRepository.getAllAuditLogs() } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = Audit.EntityType.PROJECT,
                action = Audit.ActionType.CREATE,
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = Audit.EntityType.TASK,
                action = Audit.ActionType.UPDATE,
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User2",
                entityType = Audit.EntityType.PROJECT,
                action = Audit.ActionType.DELETE,
            )
        )

        // When
        val result = getAllAuditLogsUseCase.getAllAuditLogs()

        // Then
        assertThat(result.size).isEqualTo(3)
        assertThat(result.map { it.action }).containsExactly(
            Audit.ActionType.CREATE,
            Audit.ActionType.UPDATE,
            Audit.ActionType.DELETE
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return audit logs for both project and task entities when they exist`() = runTest {
        // Given
        coEvery { auditRepository.getAllAuditLogs() } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = Audit.EntityType.PROJECT,
                entityId = "project-123",
                action = Audit.ActionType.CREATE,
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = Audit.EntityType.TASK,
                entityId = "task-456",
                action = Audit.ActionType.CREATE,
            )
        )

        // When
        val result = getAllAuditLogsUseCase.getAllAuditLogs()

        // Then
        assertThat(result.size).isEqualTo(2)
        assertThat(result.map { it.entityType }).containsExactly(
            Audit.EntityType.PROJECT,
            Audit.EntityType.TASK
        )
        assertThat(result.map { it.entityId }).containsExactly(
            "project-123",
            "task-456"
        )
    }
}
