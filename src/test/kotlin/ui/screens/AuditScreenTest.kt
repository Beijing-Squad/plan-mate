package ui.screens

import fake.createAudit
import io.mockk.*
import kotlinx.coroutines.runBlocking
import logic.entities.ActionType
import logic.entities.EntityType
import logic.entities.UserRole
import logic.exceptions.InvalidInputException
import logic.exceptions.ProjectNotFoundException
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
        ),
        createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            action = ActionType.UPDATE,
            entityType = EntityType.TASK,
        ),
        createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            action = ActionType.UPDATE,
            entityType = EntityType.PROJECT,
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
        auditScreen.showOptionService()
        verify { consoleIO.showWithLine(any()) }
    }

    @Test
    fun `handleFeatureChoice should call getAllAuditLogs when this option is selected`() = runBlocking {
        every { consoleIO.read() } returns "1"
        coEvery { getAllAuditLogsUseCase.getAllAuditLogs() } returns allAudit

        auditScreen.handleFeatureChoice()

        coVerify { getAllAuditLogsUseCase.getAllAuditLogs() }
    }

    @Test
    fun `should view no audit logs found message when getAllAuditLogs returns empty list`() = runBlocking {
        every { consoleIO.read() } returns "1"
        coEvery { getAllAuditLogsUseCase.getAllAuditLogs() } returns emptyList()

        auditScreen.handleFeatureChoice()

        verify { consoleIO.showWithLine("❌ No Audit Logs Found") }
    }

    @Test
    fun `should view all audit logs when getAllAuditLogs returns non-empty list`() = runBlocking {
        every { consoleIO.read() } returns "1"
        coEvery { getAllAuditLogsUseCase.getAllAuditLogs() } returns allAudit

        auditScreen.handleFeatureChoice()

        verifyOrder {
            consoleIO.showWithLine("\n📋 All Audit Logs:\n")
            consoleIO.showWithLine(any())
        }
    }

    @Test
    fun `handleFeatureChoice should call getAuditLogsByProjectId when this option is selected`() = runBlocking {
        every { consoleIO.read() } returns "2"
        coEvery { getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(any()) } returns allAudit

        auditScreen.handleFeatureChoice()

        coVerify { getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(any()) }
    }

    @Test
    fun `should view all project audit logs when getAuditLogsByProjectId returns non-empty list`() = runBlocking {
        val projectId = "123"
        every { consoleIO.read() } returns "2" andThen projectId
        coEvery { getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(projectId) } returns allAudit

        auditScreen.handleFeatureChoice()

        verifyOrder {
            consoleIO.showWithLine("\n🔍 Audit Logs For Project ID: $projectId\n")
            consoleIO.showWithLine(any())
        }
    }

    @Test
    fun `should view PROJECT_NOT_FOUND_ERROR message when getAuditLogsByProjectId returns error`() = runBlocking {
        val projectId = "123"
        val error = "❌ Error: Project logs not found"
        every { consoleIO.read() } returns "2" andThen projectId
        coEvery {
            getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(projectId)
        } throws ProjectNotFoundException(error)

        auditScreen.handleFeatureChoice()

        verify { consoleIO.showWithLine("❌ $error") }
    }

    @Test
    fun `should view INVALID_ID_ERROR message when project id is blank`() = runBlocking {
        val error = "Error: ID shouldn't be blank"
        every { consoleIO.read() } returns "2" andThen ""
        coEvery {
            getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId("")
        } throws InvalidInputException(error)

        auditScreen.handleFeatureChoice()

        verify { consoleIO.showWithLine("❌ $error") }
    }

    @Test
    fun `handleFeatureChoice should call getAuditLogsByTaskId when this option is selected`() = runBlocking {
        every { consoleIO.read() } returns "3"
        coEvery { getAuditLogsByTaskIdUseCase.getAuditLogsByTaskId(any()) } returns allAudit

        auditScreen.handleFeatureChoice()

        coVerify { getAuditLogsByTaskIdUseCase.getAuditLogsByTaskId(any()) }
    }

    @Test
    fun `should view all task audit logs when getAuditLogsByTaskId returns non-empty list`() = runBlocking {
        val taskId = "456"
        every { consoleIO.read() } returns "3" andThen taskId
        coEvery { getAuditLogsByTaskIdUseCase.getAuditLogsByTaskId(taskId) } returns allAudit

        auditScreen.handleFeatureChoice()

        verifyOrder {
            consoleIO.showWithLine("\n🔍 Audit Logs For Task ID: $taskId\n")
            consoleIO.showWithLine(any())
        }
    }

    @Test
    fun `should view INVALID_ID_ERROR message when task id is blank`() = runBlocking {
        val error = "Error: ID shouldn't be blank"
        every { consoleIO.read() } returns "3" andThen ""
        coEvery {
            getAuditLogsByTaskIdUseCase.getAuditLogsByTaskId("")
        } throws InvalidInputException(error)

        auditScreen.handleFeatureChoice()

        verify { consoleIO.showWithLine("❌ $error") }
    }

    @Test
    fun `should exit from audit log system when 0 is selected`() = runBlocking {
        every { consoleIO.read() } returns "0"

        auditScreen.handleFeatureChoice()

        coVerify(exactly = 0) { getAllAuditLogsUseCase.getAllAuditLogs() }
    }

    @Test
    fun `should show error message when enter invalid option`() = runBlocking {
        val invalidOption = "999"
        val invalidOptionMessage = "❌ Invalid Option"
        every { consoleIO.read() } returns invalidOption

        auditScreen.handleFeatureChoice()

        verify { consoleIO.showWithLine(invalidOptionMessage) }
    }
}
