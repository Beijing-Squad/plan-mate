package ui.screens

import com.google.common.truth.Truth.assertThat
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import fake.createState
import fake.createTask
import io.mockk.*
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import logic.entities.*
import logic.entities.type.UserRole
import logic.useCases.audit.AddAuditLogUseCase
import logic.useCases.authentication.SessionManagerUseCase
import logic.useCases.state.GetAllTaskStatesUseCase
import logic.useCases.task.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.console.SwimlanesRenderer
import ui.main.consoleIO.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class TaskManagementScreenTest {

    private lateinit var screen: TaskManagementScreen
    private val getAllTasksUseCase = mockk<GetAllTasksUseCase>()
    private val getAllTaskStatesUseCase = mockk<GetAllTaskStatesUseCase>()
    private val addTaskUseCase = mockk<AddTaskUseCase>(relaxed = true)
    private val updateTaskUseCase = mockk<UpdateTaskUseCase>(relaxed = true)
    private val deleteTaskUseCase = mockk<DeleteTaskUseCase>(relaxed = true)
    private val getTaskByIdUseCase = mockk<GetTaskByIdUseCase>()
    private val swimlanesRenderer = mockk<SwimlanesRenderer>(relaxed = true)
    private val consoleIO = mockk<ConsoleIO>(relaxed = true)
    private val sessionManagerUseCase = mockk<SessionManagerUseCase>(relaxed = true)
    private val addAuditLogUseCase = mockk<AddAuditLogUseCase>(relaxed = true)
    private val database = mockk<MongoDatabase>(relaxed = true)
    @BeforeEach
    fun setUp() {
        screen = TaskManagementScreen(
            getAllTasksUseCase = getAllTasksUseCase,
            getAllTaskStatesUseCase = getAllTaskStatesUseCase,
            addTaskUseCase = addTaskUseCase,
            deleteTaskUseCase = deleteTaskUseCase,
            getTaskByIdUseCase = getTaskByIdUseCase,
            updateTaskUseCase = updateTaskUseCase,
            swimlanesRenderer = swimlanesRenderer,
            addAudit = addAuditLogUseCase,
            consoleIO = consoleIO,
            sessionManagerUseCase = sessionManagerUseCase,
            database = database)
    }



    @Test
    fun `addTask should invoke use case with constructed task`() = runTest {
        // Given
        val user = mockk<User> {
            every { userName } returns "Zeinab"
            every { role } returns UserRole.MATE
        }
        coEvery { sessionManagerUseCase.getCurrentUser() } returns user
        coEvery { consoleIO.read() } returnsMany listOf("Title", "Description", "s1", "p1")
        val taskSlot = slot<Task>()
        coEvery { addTaskUseCase.addTask(capture(taskSlot)) } returns Unit
        val auditSlot = slot<Audit>()
        coEvery { addAuditLogUseCase.addAuditLog(capture(auditSlot)) } returns Unit

        // When
        screen.addTask()

        // Then
        with(taskSlot.captured) {
            assertThat(title).isEqualTo("Title")
            assertThat(description).isEqualTo("Description")
            assertThat(stateId).isEqualTo("s1")
            assertThat(projectId).isEqualTo("p1")
            assertThat(createdBy).isEqualTo("Zeinab")
        }
        with(auditSlot.captured) {
            assertThat(userName).isEqualTo("Zeinab")
            assertThat(action).isEqualTo(ActionType.CREATE)
            assertThat(entityType).isEqualTo(EntityType.TASK)
        }
        coVerify { consoleIO.showWithLine("✅ Task added successfully.") }
    }

    @Test
    fun `getTaskById should fetch task and show output`() = runTest {
        // Given
        val task = createTask(title = "My Task")
        coEvery { consoleIO.read() } returns task.id.toString()
        coEvery { getTaskByIdUseCase.getTaskById(task.id.toString()) } returns task

        // When
        screen.getTaskById()

        // Then
        coVerify {
            consoleIO.showWithLine(match {
                it.contains("ID: ${task.id}") &&
                        it.contains("Title: ${task.title}")
            })
        }
    }

    @Test
    fun `deleteTaskById should call deleteTask use case`() = runTest {
        // Given
        val task = createTask(title = "Task to Delete")
        coEvery { consoleIO.read() } returns task.id.toString()
        coEvery { getTaskByIdUseCase.getTaskById(task.id.toString()) } returns task
        coEvery { deleteTaskUseCase.deleteTask(task.id.toString()) } returns Unit
        val auditSlot = slot<Audit>()
        coEvery { addAuditLogUseCase.addAuditLog(capture(auditSlot)) } returns Unit
        coEvery { sessionManagerUseCase.getCurrentUser() } returns mockk {
            every { userName } returns "Zeinab"
            every { role } returns UserRole.MATE
        }

        // When
        screen.deleteTaskById()

        // Then
        coVerify { deleteTaskUseCase.deleteTask(task.id.toString()) }
        coVerify { consoleIO.showWithLine("✅ Task deleted successfully.") }
        with(auditSlot.captured) {
            assertThat(action).isEqualTo(ActionType.DELETE)
            assertThat(entityId).isEqualTo(task.id.toString())
        }
    }

    @Test
    fun `should update task when input is valid`() = runTest {
        // Given
        val oldTask = createTask(title = "Old", description = "Old Desc")
        coEvery { consoleIO.read() } returnsMany listOf(oldTask.id.toString(), "New", "New Desc", "s2")
        coEvery { getTaskByIdUseCase.getTaskById(oldTask.id.toString()) } returns oldTask
        val updatedTask = oldTask.copy(
            title = "New",
            description = "New Desc",
            stateId = "s2",
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )
        coEvery { updateTaskUseCase.updateTask(any()) } returns updatedTask
        coEvery { sessionManagerUseCase.getCurrentUser() } returns mockk {
            every { userName } returns "Zeinab"
            every { role } returns UserRole.MATE
        }
        val auditSlot = slot<Audit>()
        coEvery { addAuditLogUseCase.addAuditLog(capture(auditSlot)) } returns Unit

        // When
        screen.updateTaskById()

        // Then
        coVerify {
            updateTaskUseCase.updateTask(match {
                it.id == oldTask.id &&
                        it.title == "New" &&
                        it.description == "New Desc" &&
                        it.stateId == "s2"
            })
            consoleIO.showWithLine(match { it.contains("✅ Task updated successfully") })
        }
        with(auditSlot.captured) {
            assertThat(action).isEqualTo(ActionType.UPDATE)
            assertThat(entityId).isEqualTo(oldTask.id.toString())
        }
    }

    @Test
    fun `should show error when task ID is blank`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns ""

        // When
        screen.updateTaskById()

        // Then
        coVerify { consoleIO.showWithLine("❌ Task ID is required.") }
    }

    @Test
    fun `should keep original values when inputs are blank`() = runTest {
        // Given
        val task = createTask(title = "Original Title", description = "Original Desc", stateId = "s1")
        coEvery { consoleIO.read() } returnsMany listOf(task.id.toString(), "", "", "")
        coEvery { getTaskByIdUseCase.getTaskById(task.id.toString()) } returns task
        coEvery { updateTaskUseCase.updateTask(any()) } returns task
        coEvery { sessionManagerUseCase.getCurrentUser() } returns mockk {
            every { userName } returns "Zeinab"
            every { role } returns UserRole.MATE
        }
        val auditSlot = slot<Audit>()
        coEvery { addAuditLogUseCase.addAuditLog(capture(auditSlot)) } returns Unit

        // When
        screen.updateTaskById()

        // Then
        coVerify {
            updateTaskUseCase.updateTask(match {
                it.id == task.id &&
                        it.title == "Original Title" &&
                        it.description == "Original Desc" &&
                        it.stateId == "s1"
            })
            consoleIO.showWithLine(match { it.contains("✅ Task updated successfully") })
        }
        with(auditSlot.captured) {
            assertThat(action).isEqualTo(ActionType.UPDATE)
            assertThat(entityId).isEqualTo(task.id.toString())
        }
    }

    @Test
    fun `showTasksInSwimlanes should render tasks and states`() = runTest {
        // Given
        val tasks = listOf(createTask(title = "T1"))
        val states = listOf(createState(name = "To Do"))
        coEvery { getAllTasksUseCase.getAllTasks() } returns tasks
        coEvery { getAllTaskStatesUseCase.getAllStates() } returns states

        // When
        screen.showTasksInSwimlanes()

        // Then
        coVerify { swimlanesRenderer.render(tasks, states) }
        assertThat(states.first().name).isEqualTo("To Do")
    }


    @Test
    fun `showAllTasksList should display message when no tasks exist`() = runTest {
        // Given
        coEvery { getAllTasksUseCase.getAllTasks() } returns emptyList()

        // When
        screen.showAllTasksList()

        // Then
        coVerify {
            consoleIO.showWithLine(match { it.contains("📋 All Tasks") })
            consoleIO.showWithLine("⚠️ No tasks available.")
        }
    }

    @Test
    fun `showAllTasksList should display all task details when tasks exist`() = runTest {
        // Given
        val task = createTask(
            title = "Test Title",
            description = "Test Description",
            createdBy = "Zeinab"
        )
        coEvery { getAllTasksUseCase.getAllTasks() } returns listOf(task)

        // When
        screen.showAllTasksList()

        // Then
        coVerify {
            consoleIO.showWithLine(match { it.contains("📋 All Tasks") })
            consoleIO.showWithLine(match {
                it.contains("ID: ${task.id}") &&
                        it.contains("Title: ${task.title}") &&
                        it.contains("Description: ${task.description}") &&
                        it.contains("Created By: ${task.createdBy}")
            })
        }
    }
}