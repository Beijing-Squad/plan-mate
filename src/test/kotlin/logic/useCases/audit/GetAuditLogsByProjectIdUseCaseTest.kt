package logic.useCases.audit

import com.google.common.truth.Truth.assertThat
import fake.createAudit
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.LocalDate
import logic.entities.ActionType
import logic.entities.EntityType
import logic.entities.UserRole
import logic.entities.exceptions.InvalidInputException
import logic.entities.exceptions.ProjectNotFoundException
import logic.repository.AuditRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetAuditLogsByProjectIdUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var getAuditLogsByProjectIdUseCase: GetAuditLogsByProjectIdUseCase

    @BeforeEach
    fun setUp() {
        auditRepository = mockk(relaxed = true)
        getAuditLogsByProjectIdUseCase = GetAuditLogsByProjectIdUseCase(auditRepository)
    }

    @Test
    fun `should return list of audit logs when project id is found`() {
        // Given
        val givenId = "PROJECT-001"
        every { auditRepository.getAllAuditLogs() } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Adel",
                action = ActionType.CREATE,
                entityType = EntityType.PROJECT,
                entityId = givenId,
                timeStamp = LocalDate(2025, 4, 29)
            ),
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Adel",
                action = ActionType.UPDATE,
                entityType = EntityType.PROJECT,
                entityId = givenId,
                timeStamp = LocalDate(2025, 4, 29)
            ),
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Adel",
                action = ActionType.UPDATE,
                entityType = EntityType.PROJECT,
                entityId = "PROJECT-123",
                timeStamp = LocalDate(2025, 4, 29)
            )
        )

        // When
        val result = getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(givenId)

        // Then
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `should throw ProjectNotFoundException of audit logs when project id not found`() {
        // Given
        val givenId = "PROJECT-001"
        every { auditRepository.getAllAuditLogs() } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Adel",
                action = ActionType.CREATE,
                entityType = EntityType.PROJECT,
                entityId = "PROJECT-555",
                timeStamp = LocalDate(2025, 4, 29)
            ),
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Adel",
                action = ActionType.UPDATE,
                entityType = EntityType.PROJECT,
                entityId = "PROJECT-555",
                timeStamp = LocalDate(2025, 4, 29)
            )
        )

        // When && Then
        assertThrows<ProjectNotFoundException> {
            getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(givenId)
        }
    }

    @Test
    fun `should throw InvalidInputException when project id is blank`() {
        // Given
        val givenId = " "
        every { auditRepository.getAllAuditLogs() } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Adel",
                action = ActionType.CREATE,
                entityType = EntityType.PROJECT,
                entityId = "PROJECT-555",
                timeStamp = LocalDate(2025, 4, 29)
            ),
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Adel",
                action = ActionType.UPDATE,
                entityType = EntityType.PROJECT,
                entityId = "PROJECT-555",
                timeStamp = LocalDate(2025, 4, 29)
            )
        )

        // When && Then
        assertThrows<InvalidInputException> {
            getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(givenId)
        }
    }

    @Test
    fun `should return audit logs in timestamp order for a project`() {
        // Given
        val givenId = "PROJECT-001"
        every { auditRepository.getAllAuditLogs() } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Adel",
                action = ActionType.CREATE,
                entityType = EntityType.PROJECT,
                entityId = givenId,
                timeStamp = LocalDate(2025, 4, 29)
            ),
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Adel",
                action = ActionType.UPDATE,
                entityType = EntityType.PROJECT,
                entityId = givenId,
                timeStamp = LocalDate(2025, 4, 29)
            ),
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Adel",
                action = ActionType.UPDATE,
                entityType = EntityType.PROJECT,
                entityId = givenId,
                timeStamp = LocalDate(2025, 4, 30)
            )
        )

        // When
        val result = getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(givenId)
            .map { it.timeStamp }

        // Then
        assertThat(result).containsExactly(
            LocalDate(2025, 4, 29),
            LocalDate(2025, 4, 29),
            LocalDate(2025, 4, 30)
        ).inOrder()
    }

}