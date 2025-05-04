package ui.screens

import com.google.common.truth.Truth.assertThat
import fake.createState
import fake.createTask
import format
import io.mockk.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import logic.useCases.authentication.SessionManager
import logic.useCases.state.GetAllStatesUseCase
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
    private val getAllStatesUseCase = mockk<GetAllStatesUseCase>()
    private val addTaskUseCase = mockk<AddTaskUseCase>(relaxed = true)
    private val updateTaskUseCase = mockk<UpdateTaskUseCase>(relaxed = true)
    private val deleteTaskUseCase = mockk<DeleteTaskUseCase>(relaxed = true)
    private val getTaskByIdUseCase = mockk<GetTaskByIdUseCase>()
    private val swimlanesRenderer = mockk<SwimlanesRenderer>(relaxed = true)
    private val consoleIO = mockk<ConsoleIO>(relaxed = true)
    private val sessionManager = mockk<SessionManager>(relaxed = true)

    @BeforeEach
    fun setUp() {
        screen = TaskManagementScreen(
            getAllTasksUseCase,
            getAllStatesUseCase,
            addTaskUseCase,
            deleteTaskUseCase,
            getTaskByIdUseCase,
            updateTaskUseCase,
            swimlanesRenderer,
            consoleIO,
            sessionManager
        )
    }


    @Test
    fun `should display all task management options when showOptionService is called`() {
        // When
        screen.showOptionService()

        // Then
        verify {
            consoleIO.showWithLine(match { it.contains("Task Management") })
            consoleIO.showWithLine(match { it.contains("1. Show All Tasks (Swimlanes)") })
            consoleIO.showWithLine(match { it.contains("2. Add Task") })
            consoleIO.showWithLine(match { it.contains("3. Find Task by ID") })
            consoleIO.showWithLine(match { it.contains("4. Delete Task") })
            consoleIO.showWithLine(match { it.contains("5. Show All Tasks (List View)") })
            consoleIO.showWithLine(match { it.contains("6. Update Task") })
        }
        verify { consoleIO.show("💡 Please enter your choice: ") }
    }

    @Test
    fun `addTask should invoke use case with constructed task`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("Title", "Description", "s1", "p1", "Zeinab")
        val taskSlot = slot<logic.entities.Task>()
        every { addTaskUseCase.addTask(capture(taskSlot)) } just Runs

        // When
        screen.addTask()

        // Then
        assertThat(taskSlot.captured.title).isEqualTo("Title")
    }

    @Test
    fun `getTaskById should fetch task and show output`() {
        // Given
        val task = createTask(title = "My Task")
        every { consoleIO.read() } returns task.id.toString()
        every { getTaskByIdUseCase.getTaskById(task.id.toString()) } returns task

        // When
        screen.getTaskById()

        // Then
        assertThat(task.title).isEqualTo("My Task")
    }

    @Test
    fun `deleteTaskById should call deleteTask use case`() {
        // Given
        every { consoleIO.read() } returns "task-id-123"

        // When
        screen.deleteTaskById()

        // Then
        verify { deleteTaskUseCase.deleteTask("task-id-123") }
        assertThat("task-id-123").isEqualTo("task-id-123")
    }
    @Test
    fun `should update task when input is valid`() {
        // Given
        val oldTask = createTask(title = "Old", description = "Old Desc")
        every { consoleIO.read() } returnsMany listOf(oldTask.id.toString(), "New", "New Desc")
        every { getTaskByIdUseCase.getTaskById(oldTask.id.toString()) } returns oldTask

        val expectedTaskToUpdate = oldTask.copy(
            title = "New",
            description = "New Desc",
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )

        val updatedTask = expectedTaskToUpdate.copy()
        every { updateTaskUseCase.updateTask(match {
            it.id == oldTask.id &&
                    it.title == "New" &&
                    it.description == "New Desc"
        }) } returns updatedTask

        // When
        screen.updateTaskById()

        // Then
        verify {
            updateTaskUseCase.updateTask(match {
                it.id == oldTask.id &&
                        it.title == "New" &&
                        it.description == "New Desc"
            })
        }
        verify { consoleIO.showWithLine(match { it.contains("✅ Task updated successfully") }) }
    }

    @Test
    fun `should show error when task ID is blank`() {
        // Given
        every { consoleIO.read() } returns ""

        // When
        screen.updateTaskById()

        // Then
        verify { consoleIO.showWithLine("❌ Task ID is required.") }
    }

    @Test
    fun `should show error when task not found`() {
        // Given
        every { consoleIO.read() } returns "non-existing-id"
        every { getTaskByIdUseCase.getTaskById("non-existing-id") } throws RuntimeException("Task not found")

        // When
        screen.updateTaskById()

        // Then
        verify { consoleIO.showWithLine("❌ Failed to update task: Task not found") }
    }

    @Test
    fun `should show error when update use case throws exception`() {
        // Given
        val task = createTask()
        every { consoleIO.read() } returnsMany listOf(task.id.toString(), "New Title", "New Desc")
        every { getTaskByIdUseCase.getTaskById(task.id.toString()) } returns task

        // Now we expect the exception when passing a Task object
        every { updateTaskUseCase.updateTask(any()) } throws RuntimeException("Unexpected error")

        // When
        screen.updateTaskById()

        // Then
        verify { consoleIO.showWithLine("❌ Failed to update task: Unexpected error") }
    }

    @Test
    fun `should keep original values when inputs are blank`() {
        // Given
        val task = createTask(title = "Original Title", description = "Original Desc")
        every { consoleIO.read() } returnsMany listOf(task.id.toString(), "", "")
        every { getTaskByIdUseCase.getTaskById(task.id.toString()) } returns task

        val expectedTaskToUpdate = task.copy(
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )

        val updatedTask = expectedTaskToUpdate.copy()
        every { updateTaskUseCase.updateTask(match {
            it.id == task.id &&
                    it.title == "Original Title" &&
                    it.description == "Original Desc"
        }) } returns updatedTask

        // When
        screen.updateTaskById()

        // Then
        verify {
            updateTaskUseCase.updateTask(match {
                it.id == task.id &&
                        it.title == "Original Title" &&
                        it.description == "Original Desc"
            })
        }
        verify { consoleIO.showWithLine(match { it.contains("✅ Task updated successfully") }) }
    }
    @Test
    fun `showTasksInSwimlanes should render tasks and states`() {
        // Given
        val tasks = listOf(createTask(title = "T1"))
        val states = listOf(createState(name = "To Do"))
        every { getAllTasksUseCase.getAllTasks() } returns tasks
        every { getAllStatesUseCase.getAllStates() } returns states

        // When
        screen.showTasksInSwimlanes()

        // Then
        verify { swimlanesRenderer.render(tasks, states) }
        assertThat(states.first().name).isEqualTo("To Do")
    }


    @Test
    fun `handleFeatureChoice should trigger correct actions for all options`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("1", "2", "3", "4", "invalid", "0")

        every { getAllTasksUseCase.getAllTasks() } returns emptyList()
        every { getAllStatesUseCase.getAllStates() } returns emptyList()
        every { getTaskByIdUseCase.getTaskById(any()) } returns createTask()
        every { consoleIO.read() } returnsMany listOf("1", "2", "3", "4", "invalid", "0") // ensure input is reused

        // When
        repeat(6) {
            screen.handleFeatureChoice()
        }

        // Then
        verify { swimlanesRenderer.render(any(), any()) }
        verify { addTaskUseCase.addTask(any()) }

    }
    @Test
    fun `showAllTasksList should display message when no tasks exist`() {
        // Given
        every { getAllTasksUseCase.getAllTasks() } returns emptyList()

        // When
        screen.showAllTasksList()

        // Then
        verify {
            consoleIO.showWithLine(match { it.contains("📋 All Tasks") })
            consoleIO.showWithLine("⚠️ No tasks available.")
        }
    }

    @Test
    fun `showAllTasksList should display all task details when tasks exist`() {
        // Given
        val task = createTask(
            title = "Test Title",
            description = "Test Description",
            createdBy = "Zeinab"
        )
        every { getAllTasksUseCase.getAllTasks() } returns listOf(task)

        // When
        screen.showAllTasksList()

        // Then
        verify {
            consoleIO.showWithLine(match { it.contains("📋 All Tasks") })
            consoleIO.showWithLine(match {
                it.contains("ID: ${task.id}") &&
                        it.contains("Title: ${task.title}") &&
                        it.contains("Description: ${task.description}") &&
                        it.contains("Created By: ${task.createdBy}") &&
                        it.contains("Created At: ${task.createdAt.format()}") &&
                        it.contains("Updated At: ${task.updatedAt.format()}")
            })
        }
    }
}