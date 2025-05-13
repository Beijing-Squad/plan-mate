package logic.useCases.audit

import GetAuditLogsByProjectIdUseCase
import com.google.common.truth.Truth.assertThat
import fake.createAudit
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.Audit
import logic.entities.type.UserRole
import logic.repository.AuditRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAuditLogsByProjectIdUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var getAuditLogsByProjectIdUseCase: GetAuditLogsByProjectIdUseCase

    private val allAudit = listOf(
        createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            action = Audit.ActionType.CREATE,
            entityType = Audit.EntityType.PROJECT,
        ),
        createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            action = Audit.ActionType.UPDATE,
            entityType = Audit.EntityType.PROJECT,
        ),
        createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            action = Audit.ActionType.UPDATE,
            entityType = Audit.EntityType.PROJECT,
        )
    )

    @BeforeEach
    fun setUp() {
        auditRepository = mockk(relaxed = true)
        getAuditLogsByProjectIdUseCase = GetAuditLogsByProjectIdUseCase(auditRepository)
    }

    @Test
    fun `should return list of audit logs when project id is found`() = runTest {
        // Given
        val givenId = "PROJECT-001"
        coEvery { auditRepository.getAllAuditLogs() } returns allAudit
        coEvery { auditRepository.getAuditLogsByProjectId(givenId) } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Adel",
                action = Audit.ActionType.CREATE,
                entityType = Audit.EntityType.PROJECT,
                entityId = givenId,
            ),
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Adel",
                action = Audit.ActionType.UPDATE,
                entityType = Audit.EntityType.PROJECT,
                entityId = givenId,
            )
        )

        // When
        val result = getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(givenId)

        // Then
        assertThat(result?.size).isEqualTo(2)
    }

    @Test
    fun `should throw ProjectNotFoundException when project id not found`() = runTest {
        // Given
        val givenId = "PROJECT-001"
        coEvery { auditRepository.getAllAuditLogs() } returns allAudit
        coEvery { auditRepository.getAuditLogsByProjectId(givenId) } returns emptyList()

        // When
        val result = getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(givenId)

        // Then
        assertThat(result).isNull()
    }


    @Test
    fun `should throw InvalidInputException when project id is blank`() = runTest {
        // Given
        val givenId = " "
        coEvery { auditRepository.getAllAuditLogs() } returns allAudit

        // When
        val result = getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(givenId)

        // Then
        assertThat(result).isNull()
    }
}
