package ui.console

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.junit.jupiter.api.*
import ui.main.consoleIO.ConsoleIO
import fake.createTask
import fake.createState
import kotlin.uuid.ExperimentalUuidApi

class SwimlanesRendererTest {

    private lateinit var consoleIO: ConsoleIO
    private lateinit var swimlanesRenderer: SwimlanesRenderer
    private val output = mutableListOf<String>()

    @BeforeEach
    fun setUp() {
        consoleIO = mockk()
        swimlanesRenderer = SwimlanesRenderer(consoleIO)
        every { consoleIO.showWithLine(capture(output)) } just Runs
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
        output.clear()
    }

    @Test
    fun `render shows warning when states are empty`() {
        val task = createTask()
        swimlanesRenderer.render(
            tasks = listOf(task),
            taskStates = emptyList()
        )

        assertThat(output).containsExactly("⚠️ No states available.")
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `render shows warning when tasks are empty`() {
        val state = createState()
        swimlanesRenderer.render(
            tasks = emptyList(),
            taskStates = listOf(state)
        )

        assertThat(output).containsExactly("⚠️ No tasks available.")
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `render prints tasks under each state`() {
        val state1 = createState(name = "To Do")
        val state2 = createState(name = "Done")
        val task1 = createTask(title = "Task A", stateId = state1.id.toString())
        val task2 = createTask(title = "Task B", stateId = state2.id.toString())

        swimlanesRenderer.render(listOf(task1, task2), listOf(state1, state2))

        assertThat(output[0]).contains("To Do")
        assertThat(output[0]).contains("Done")
        assertThat(output[1]).isEqualTo("-".repeat(output[0].length))
        assertThat(output[2]).contains("#1: Task A")
        assertThat(output[2]).contains("#1: Task B")
        assertThat(output).hasSize(3)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `render handles multiple tasks in a single state`() {
        val state1 = createState(name = "Backlog")
        val state2 = createState(name = "Review")
        val task1 = createTask(title = "Task A", stateId = state1.id.toString())
        val task2 = createTask(title = "Task B", stateId = state1.id.toString())
        val task3 = createTask(title = "Task C", stateId = state2.id.toString())

        swimlanesRenderer.render(listOf(task1, task2, task3), listOf(state1, state2))

        assertThat(output[2]).contains("#1: Task A")
        assertThat(output[2]).contains("#1: Task C")
        assertThat(output[3]).contains("#2: Task B")
        assertThat(output[3]).contains(" ".repeat(20))
        assertThat(output).hasSize(4)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `render truncates long task titles to fit column`() {
        val longTitle = "Very long task title that should be trimmed"
        val state = createState(name = "TrimTest")
        val task = createTask(title = longTitle, stateId = state.id.toString())

        swimlanesRenderer.render(listOf(task), listOf(state))

        val maxLength = 14
        val expectedTruncated = "#1: $longTitle".take(maxLength).padEnd(20)
        assertThat(output[2]).isEqualTo(expectedTruncated)
    }
}
