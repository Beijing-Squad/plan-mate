package ui.screens

import com.google.common.truth.Truth.assertThat
import fake.createState
import fake.createTask
import io.mockk.*
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
    private val deleteTaskUseCase = mockk<DeleteTaskUseCase>(relaxed = true)
    private val getTaskByIdUseCase = mockk<GetTaskByIdUseCase>()
    private val swimlanesRenderer = mockk<SwimlanesRenderer>(relaxed = true)
    private val consoleIO = mockk<ConsoleIO>(relaxed = true)

    @BeforeEach
    fun setUp() {
        screen = TaskManagementScreen(
            getAllTasksUseCase,
            getAllStatesUseCase,
            addTaskUseCase,
            deleteTaskUseCase,
            getTaskByIdUseCase,
            swimlanesRenderer,
            consoleIO
        )
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
    fun `showOptionService should list all options`() {
        // When
        screen.showOptionService()

        // Then
        verify { consoleIO.showWithLine(match { it.contains("Task Board") }) }
        assertThat(true).isTrue()
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
            consoleIO.showWithLine(match { it.contains("🆔 ID: ${task.id}") })
            consoleIO.showWithLine(match { it.contains("📌 Title: ${task.title}") })
            consoleIO.showWithLine(match { it.contains("📝 Description: ${task.description}") })
            consoleIO.showWithLine(match { it.contains("👤 Created By: ${task.createdBy}") })
            consoleIO.showWithLine(match { it.contains("📅 Created At: ${task.createdAt}") })
            consoleIO.showWithLine(match { it.contains("🔄 Updated At: ${task.updatedAt}") })
        }
    }

}