package ui.screens

import com.google.common.truth.Truth.assertThat
import fake.createState
import fake.createTask
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.entities.*
import logic.exceptions.TaskNotFoundException
import logic.exceptions.TaskAlreadyExistsException
import logic.exceptions.TaskException
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
        )
    }

    @Test
    fun `should display tasks in swimlanes when tasks exist`() = runTest {
        // Given
        val tasks = listOf(createTask(title = "Task 1"))
        val states = listOf(createState(name = "To Do"))
        coEvery { getAllTasksUseCase.getAllTasks() } returns tasks
        coEvery { getAllTaskStatesUseCase.getAllStates() } returns states

        // When
        screen.showTasksInSwimlanes()

        // Then
        coVerify { swimlanesRenderer.render(tasks, states) }
    }

    @Test
    fun `should show no tasks message when task list is empty`() = runTest {
        // Given
        coEvery { getAllTasksUseCase.getAllTasks() } returns emptyList()

        // When
        screen.showAllTasksList()

        // Then
        coVerify { consoleIO.showWithLine("⚠️ No tasks available.") }
    }

    @Test
    fun `should display task details when task exists`() = runTest {
        // Given
        val task = createTask(title = "Test Task")
        coEvery { consoleIO.read() } returns task.id.toString()
        coEvery { getTaskByIdUseCase.getTaskById(task.id.toString()) } returns task

        // When
        screen.getTaskById()

        // Then
        coVerify { consoleIO.showWithLine(match { it.contains("Test Task") }) }
    }

    @Test
    fun `should show error when task not found`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "non-existent-id"
        coEvery { getTaskByIdUseCase.getTaskById("non-existent-id") } throws TaskNotFoundException("Task not found")

        // When
        screen.getTaskById()

        // Then
        coVerify { consoleIO.showWithLine(match { it.contains("❌ Task not found") }) }
    }

    @Test
    fun `should show error when no user is logged in`() = runTest {
        // Given
        coEvery { sessionManagerUseCase.getCurrentUser() } returns null

        // When
        screen.addTask()

        // Then
        coVerify { consoleIO.showWithLine("❌ No user is currently logged in.") }
    }

    @Test
    fun `should show error when required fields are missing`() = runTest {
        // Given
        val user = mockk<User> {
            every { userName } returns "TestUser"
            every { role } returns UserRole.MATE
        }
        coEvery { sessionManagerUseCase.getCurrentUser() } returns user
        coEvery { consoleIO.read() } returnsMany listOf("", "", "", "")

        // When
        screen.addTask()

        // Then
        coVerify { consoleIO.showWithLine("❌ Title, State ID, and Project ID are required.") }
    }

    @Test
    fun `should show error when task already exists`() = runTest {
        // Given
        val user = mockk<User> {
            every { userName } returns "TestUser"
            every { role } returns UserRole.MATE
        }
        coEvery { sessionManagerUseCase.getCurrentUser() } returns user
        coEvery { consoleIO.read() } returnsMany listOf("Title", "Description", "state1", "project1")
        coEvery { addTaskUseCase.addTask(any()) } throws TaskAlreadyExistsException("Task already exists")

        // When
        screen.addTask()

        // Then
        coVerify { consoleIO.showWithLine(match { it.contains("❌ Task already exists") }) }
    }

    @Test
    fun `should show error when task ID is empty`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns ""

        // When
        screen.deleteTaskById()

        // Then
        coVerify { consoleIO.showWithLine("❌ Task ID is required.") }
    }

    @Test
    fun `should show error when task has dependencies`() = runTest {
        // Given
        val task = createTask(title = "Task with Dependencies")
        coEvery { consoleIO.read() } returns task.id.toString()
        coEvery { getTaskByIdUseCase.getTaskById(task.id.toString()) } returns task
        coEvery { deleteTaskUseCase.deleteTask(task.id.toString()) } throws TaskException("Cannot delete task with dependencies")

        // When
        screen.deleteTaskById()

        // Then
        coVerify { consoleIO.showWithLine(match { it.contains("❌ Error deleting task") }) }
    }

    @Test
    fun `should handle tasks with special characters`() = runTest {
        // Given
        val tasks = listOf(
            createTask(title = "Task with !@#$%^&*()"),
            createTask(title = "Task with 你好"),
            createTask(title = "Task with 😊")
        )
        coEvery { getAllTasksUseCase.getAllTasks() } returns tasks

        // When
        screen.showAllTasksList()

        // Then
        coVerify { 
            consoleIO.showWithLine(match { it.contains("Task with !@#$%^&*()") })
            consoleIO.showWithLine(match { it.contains("Task with 你好") })
            consoleIO.showWithLine(match { it.contains("Task with 😊") })
        }
    }

    @Test
    fun `should handle task with long description`() = runTest {
        // Given
        val longDescription = "a".repeat(1000)
        val task = createTask(title = "Test Task", description = longDescription)
        coEvery { consoleIO.read() } returns task.id.toString()
        coEvery { getTaskByIdUseCase.getTaskById(task.id.toString()) } returns task

        // When
        screen.getTaskById()

        // Then
        coVerify { 
            consoleIO.showWithLine(match { it.contains("Test Task") })
            consoleIO.showWithLine(match { it.contains(longDescription) })
        }
    }

    @Test
    fun `should handle tasks with different states`() = runTest {
        // Given
        val tasks = listOf(
            createTask(title = "Task 1", stateId = "todo"),
            createTask(title = "Task 2", stateId = "in_progress"),
            createTask(title = "Task 3", stateId = "done")
        )
        val states = listOf(
            createState(name = "To Do"),
            createState(name = "In Progress"),
            createState(name = "Done")
        )
        coEvery { getAllTasksUseCase.getAllTasks() } returns tasks
        coEvery { getAllTaskStatesUseCase.getAllStates() } returns states

        // When
        screen.showTasksInSwimlanes()

        // Then
        coVerify { swimlanesRenderer.render(tasks, states) }
    }

    @Test
    fun `should handle task with maximum length fields`() = runTest {
        // Given
        val user = mockk<User> {
            every { userName } returns "TestUser"
            every { role } returns UserRole.MATE
        }
        val longTitle = "a".repeat(100)
        val longDescription = "b".repeat(1000)
        coEvery { sessionManagerUseCase.getCurrentUser() } returns user
        coEvery { consoleIO.read() } returnsMany listOf(longTitle, longDescription, "state1", "project1")
        val taskSlot = slot<Task>()
        coEvery { addTaskUseCase.addTask(capture(taskSlot)) } returns Unit

        // When
        screen.addTask()

        // Then
        coVerify { 
            addTaskUseCase.addTask(any())
            consoleIO.showWithLine("✅ Task added successfully.")
        }
        with(taskSlot.captured) {
            assertThat(title).isEqualTo(longTitle)
            assertThat(description).isEqualTo(longDescription)
        }
    }

    @Test
    fun `should handle task with all fields updated`() = runTest {
        // Given
        val oldTask = createTask(
            title = "Old Title",
            description = "Old Description",
            stateId = "old_state"
        )
        coEvery { consoleIO.read() } returnsMany listOf(
            oldTask.id.toString(),
            "New Title",
            "New Description",
            "new_state"
        )
        coEvery { getTaskByIdUseCase.getTaskById(oldTask.id.toString()) } returns oldTask
        val taskSlot = slot<Task>()
        coEvery { updateTaskUseCase.updateTask(capture(taskSlot)) } returns oldTask.copy(
            title = "New Title",
            description = "New Description",
            stateId = "new_state"
        )

        // When
        screen.updateTaskById()

        // Then
        coVerify { 
            updateTaskUseCase.updateTask(any())
            consoleIO.showWithLine(match { it.contains("✅ Task updated successfully") })
        }
        with(taskSlot.captured) {
            assertThat(title).isEqualTo("New Title")
            assertThat(description).isEqualTo("New Description")
            assertThat(stateId).isEqualTo("new_state")
        }
    }

    @Test
    fun `should handle whitespace only inputs`() = runTest {
        // Given
        val user = mockk<User> {
            every { userName } returns "TestUser"
            every { role } returns UserRole.MATE
        }
        coEvery { sessionManagerUseCase.getCurrentUser() } returns user
        coEvery { consoleIO.read() } returnsMany listOf("   ", "   ", "   ", "   ")

        // When
        screen.addTask()

        // Then
        coVerify { consoleIO.showWithLine("❌ Title, State ID, and Project ID are required.") }
    }

    @Test
    fun `should handle TaskException from states`() = runTest {
        // Given
        val tasks = listOf(createTask(title = "Task 1"))
        coEvery { getAllTasksUseCase.getAllTasks() } returns tasks
        coEvery { getAllTaskStatesUseCase.getAllStates() } throws TaskException("Failed to load states")

        // When
        screen.showTasksInSwimlanes()

        // Then
        coVerify { consoleIO.showWithLine(match { it.contains("❌ Failed to load tasks") }) }
    }

    @Test
    fun `should handle invalid task ID format`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "invalid-id-format"
        coEvery { getTaskByIdUseCase.getTaskById("invalid-id-format") } throws TaskException("Invalid task ID format")

        // When
        screen.getTaskById()

        // Then
        coVerify { consoleIO.showWithLine(match { it.contains("❌ Error retrieving task") }) }
    }
}