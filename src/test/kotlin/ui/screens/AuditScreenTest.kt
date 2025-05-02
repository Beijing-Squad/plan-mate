package ui.screens

import fake.createAudit
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.datetime.LocalDate
import logic.entities.ActionType
import logic.entities.EntityType
import logic.entities.UserRole
import logic.entities.exceptions.InvalidInputException
import logic.entities.exceptions.ProjectNotFoundException
import logic.useCases.audit.GetAllAuditLogsUseCase
import logic.useCases.audit.GetAuditLogsByProjectIdUseCase
import logic.useCases.audit.GetAuditLogsByTaskIdUseCase
import ui.main.consoleIO.ConsoleIO
import kotlin.test.BeforeTest
import kotlin.test.Test

class AuditScreenTest {

    private lateinit var getAllAuditLogsUseCase: GetAllAuditLogsUseCase
    private lateinit var getAuditLogsByProjectIdUseCase: GetAuditLogsByProjectIdUseCase
    private lateinit var getAuditLogsByTaskIdUseCase: GetAuditLogsByTaskIdUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var auditScreen: AuditScreen

    private val allAudit = listOf(
        createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            action = ActionType.CREATE,
            entityType = EntityType.PROJECT,
            timeStamp = LocalDate(2025, 4, 29)
        ),
        createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            action = ActionType.UPDATE,
            entityType = EntityType.TASK,
            oldState = "Review",
            newState = "Done",
            timeStamp = LocalDate(2025, 4, 29)
        ),
        createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            action = ActionType.UPDATE,
            entityType = EntityType.PROJECT,
            timeStamp = LocalDate(2025, 4, 29)
        )
    )

    @BeforeTest
    fun setUp() {
        getAllAuditLogsUseCase = mockk(relaxed = true)
        getAuditLogsByProjectIdUseCase = mockk(relaxed = true)
        getAuditLogsByTaskIdUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)

        auditScreen = AuditScreen(
            getAllAuditLogsUseCase,
            getAuditLogsByProjectIdUseCase,
            getAuditLogsByTaskIdUseCase,
            consoleIO
        )
    }

    @Test
    fun `showOptionService should display audit logs options when called`() {
        // Given && When
        auditScreen.showOptionService()

        // Then
        verify { consoleIO.showWithLine(any()) }
    }

    @Test
    fun `handleFeatureChoice should call getAllAuditLogs when this option is selected`() {
        // Given
        val getAllAuditLogsOption = "1"
        every { consoleIO.read() } returns getAllAuditLogsOption

        // When
        auditScreen.handleFeatureChoice()

        // Then
        verify { getAllAuditLogsUseCase.getAllAuditLogs() }
    }


























}