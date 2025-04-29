package logic.useCases.audit

import com.google.common.truth.Truth.assertThat
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

class GetAuditLogsByProjectIdUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var getAuditLogsByProjectIdUseCase: GetAuditLogsByProjectIdUseCase

    @BeforeEach
    fun setUp() {
        auditRepository = mockk()
        getAuditLogsByProjectIdUseCase = GetAuditLogsByProjectIdUseCase(auditRepository)
    }

    @Test
    fun `should return list of audit logs when given project id is valid`() {
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
            )
        )

        // When
        val result = getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(givenId)

        // Then
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `should return empty list of audit logs when given project id is invalid`() {
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

        // When
        val result = getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(givenId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list of audit logs when audit repository is empty`() {
        // Given
        val givenId = "PROJECT-001"
        every { auditRepository.getAllAuditLogs() } returns emptyList()

        // When
        val result = getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(givenId)

        // Then
        assertThat(result).isEmpty()
    }



}