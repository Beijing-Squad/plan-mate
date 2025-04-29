package logic.useCases.audit

import com.google.common.truth.Truth.assertThat
import data.repository.AuditRepositoryImpl
import fake.createAudit
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import logic.entities.EntityType
import logic.entities.UserRole
import logic.repository.AuditRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class GetAllAuditLogsUseCaseTest {

    lateinit var getAllAuditLogs: GetAllAuditLogsUseCase
    lateinit var auditRepository: AuditRepository

    @BeforeEach
    fun setUp() {
        auditRepository = AuditRepositoryImpl(mockk(relaxed = true))
        getAllAuditLogs = GetAllAuditLogsUseCase(auditRepository)
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
        val result = auditRepository.getAllAuditLogs()

        // Then
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `should return empty list when audit repo is empty`() {
        // Given
        every { auditRepository.getAllAuditLogs() } returns emptyList()

        // When
        val result = auditRepository.getAllAuditLogs()

        // Then
        assertThat(result.size).isEqualTo(0)
    }
}