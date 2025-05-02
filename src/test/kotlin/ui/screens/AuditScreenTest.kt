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

    @Test
    fun `should view no audit logs found message when getAllAuditLogs returns empty list`() {
        // Given
        val getAllAuditLogsOption = "1"
        every { consoleIO.read() } returns getAllAuditLogsOption
        every { getAllAuditLogsUseCase.getAllAuditLogs() } returns emptyList()

        // When
        auditScreen.handleFeatureChoice()

        // Then
        verify { consoleIO.showWithLine("❌ No Audit Logs Found") }
    }

    @Test
    fun `should view all audit logs when getAllAuditLogs returns non-empty list`() {
        // Given
        val getAllAuditLogsOption = "1"
        every { consoleIO.read() } returns getAllAuditLogsOption
        every { getAllAuditLogsUseCase.getAllAuditLogs() } returns allAudit

        // When
        auditScreen.handleFeatureChoice()

        // Then
        verifyOrder {
            consoleIO.showWithLine("\n📋 All Audit Logs:\n")
            consoleIO.showWithLine(any())
        }
    }


    @Test
    fun `handleFeatureChoice should call getAuditLogsByProjectId when this option is selected`() {
        // Given
        val getAuditLogsByProjectIdOption = "2"
        every { consoleIO.read() } returns getAuditLogsByProjectIdOption

        // When
        auditScreen.handleFeatureChoice()

        // Then
        verify { getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(any()) }
    }

    @Test
    fun `should view all project audit logs when getAuditLogsByProjectId returns non-empty list`() {
        // Given
        val getAuditLogsByProjectIdOption = "2"
        val projectId = "123"
        every { consoleIO.read() } returns getAuditLogsByProjectIdOption andThen projectId
        every { getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(projectId) } returns allAudit

        // When
        auditScreen.handleFeatureChoice()

        // Then
        verifyOrder {
            consoleIO.showWithLine("\n🔍 Audit Logs For Project ID: $projectId\n")
            consoleIO.showWithLine(any())
        }
    }

    @Test
    fun `should view PROJECT_NOT_FOUND_ERROR message when getAuditLogsByProjectId returns empty list`() {
        // Given
        val getAuditLogsByProjectIdOption = "2"
        val projectId = "123"
        val projectNotFoundError = "❌ Error: Project logs not found"
        every { consoleIO.read() } returns getAuditLogsByProjectIdOption andThen projectId
        every {
            getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(projectId)
        } throws ProjectNotFoundException(projectNotFoundError)


        // When
        auditScreen.handleFeatureChoice()

        // Then
        verify { consoleIO.showWithLine("❌ $projectNotFoundError") }
    }

    @Test
    fun `should view INVALID_ID_ERROR message when project id is blank`() {
        // Given
        val getAuditLogsByProjectIdOption = "2"
        val projectId = ""
        val INVALID_ID_ERROR = "Error: ID shouldn't be blank"
        every { consoleIO.read() } returns getAuditLogsByProjectIdOption andThen projectId
        every {
            getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(projectId)
        } throws InvalidInputException(INVALID_ID_ERROR)


        // When
        auditScreen.handleFeatureChoice()

        // Then
        verify { consoleIO.showWithLine("❌ $INVALID_ID_ERROR") }
    }


    @Test
    fun `handleFeatureChoice should call getAuditLogsByTaskId when this option is selected`() {
        // Given
        val getAuditLogsByTaskIdOption = "3"
        every { consoleIO.read() } returns getAuditLogsByTaskIdOption

        // When
        auditScreen.handleFeatureChoice()

        // Then
        verify { getAuditLogsByTaskIdUseCase.getAuditLogsByTaskId(any()) }
    }

    @Test
    fun `should view all task audit logs when getAuditLogsByTaskId returns non-empty list`() {
        // Given
        val getAuditLogsByTaskIdOption = "3"
        val taskId = "456"
        every { consoleIO.read() } returns getAuditLogsByTaskIdOption andThen taskId
        every { getAuditLogsByTaskIdUseCase.getAuditLogsByTaskId(taskId) } returns allAudit

        // When
        auditScreen.handleFeatureChoice()

        // Then
        verifyOrder {
            consoleIO.showWithLine("\n🔍 Audit Logs For Task ID: $taskId\n")
            consoleIO.showWithLine(any())
        }
    }








}