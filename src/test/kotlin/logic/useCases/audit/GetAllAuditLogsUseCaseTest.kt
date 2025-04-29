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
import org.junit.jupiter.api.Test

class GetAllAuditLogsUseCaseTest {

    lateinit var getAllAuditLogsUseCase: GetAllAuditLogsUseCase
    lateinit var auditRepository: AuditRepository

    @BeforeEach
    fun setUp() {
        auditRepository = AuditRepositoryImpl(mockk(relaxed = true))
        getAllAuditLogsUseCase = GetAllAuditLogsUseCase(auditRepository)
    }

    @Test
    fun `should return all audit logs when called`() {
        // Given
        every { auditRepository.getAllAuditLogs() } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.PROJECT,
                timeStamp = LocalDate(2023, 1, 1)
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "MATE",
                entityType = EntityType.TASK,
                timeStamp = LocalDate(2023, 1, 12)
            )
        )

        // When
        val result = getAllAuditLogsUseCase.getAllAuditLogs()

        // Then
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `should return empty list when audit repo is empty`() {
        // Given
        every { auditRepository.getAllAuditLogs() } returns emptyList()

        // When
        val result = getAllAuditLogsUseCase.getAllAuditLogs()

        // Then
        assertThat(result.size).isEqualTo(0)
    }

    @Test
    fun `should return audit logs with different action types`() {
        // Given
        every { auditRepository.getAllAuditLogs() } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.PROJECT,
                action = ActionType.CREATE,
                timeStamp = LocalDate(2023, 1, 1)
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = EntityType.TASK,
                action = ActionType.UPDATE,
                timeStamp = LocalDate(2023, 1, 2)
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User2",
                entityType = EntityType.PROJECT,
                action = ActionType.DELETE,
                timeStamp = LocalDate(2023, 1, 3)
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
    fun `should return audit logs with old and new state values`() {
        // Given
        every { auditRepository.getAllAuditLogs() } returns listOf(
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = EntityType.TASK,
                action = ActionType.UPDATE,
                oldState = "In Progress",
                newState = "Completed",
                timeStamp = LocalDate(2023, 1, 1)
            ),
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.PROJECT,
                action = ActionType.UPDATE,
                oldState = "Draft",
                newState = "Active",
                timeStamp = LocalDate(2023, 1, 2)
            )
        )

        // When
        val result = getAllAuditLogsUseCase.getAllAuditLogs()

        // Then
        assertThat(result.size).isEqualTo(2)
        assertThat(result[0].oldState).isEqualTo("In Progress")
        assertThat(result[0].newState).isEqualTo("Completed")
        assertThat(result[1].oldState).isEqualTo("Draft")
        assertThat(result[1].newState).isEqualTo("Active")
    }

    @Test
    fun `should return audit logs for both project and task entities`() {
        // Given
        every { auditRepository.getAllAuditLogs() } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.PROJECT,
                entityId = "project-123",
                action = ActionType.CREATE,
                timeStamp = LocalDate(2023, 1, 1)
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = EntityType.TASK,
                entityId = "task-456",
                action = ActionType.CREATE,
                timeStamp = LocalDate(2023, 1, 2)
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

    @Test
    fun `should return audit logs sorted by timestamp`() {
        // Given
        every { auditRepository.getAllAuditLogs() } returns listOf(
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = EntityType.TASK,
                timeStamp = LocalDate(2023, 1, 3)
            ),
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.PROJECT,
                timeStamp = LocalDate(2023, 1, 1)
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User2",
                entityType = EntityType.TASK,
                timeStamp = LocalDate(2023, 1, 2)
            )
        )

        // When
        val result = getAllAuditLogsUseCase.getAllAuditLogs()

        // Then
        assertThat(result.size).isEqualTo(3)
        assertThat(result.map { it.timeStamp }).containsExactly(
            LocalDate(2023, 1, 3),
            LocalDate(2023, 1, 1),
            LocalDate(2023, 1, 2)
        ).inOrder()
    }
}