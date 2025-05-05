package data.csvDataSource

import com.google.common.truth.Truth.assertThat
import data.csvDataSource.csv.CsvDataSourceImpl
import fake.createTask
import io.mockk.*
import kotlinx.datetime.LocalDateTime
import logic.entities.Task
import logic.entities.exceptions.TaskNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class TasksCsvDataSourceImplTest {

    private lateinit var csvDataSource: CsvDataSourceImpl<Task>
    private lateinit var tasksCsvDataSource: TasksCsvDataSourceImpl

    @BeforeEach
    fun setUp() {
        csvDataSource = mockk(relaxed = true)
        tasksCsvDataSource = TasksCsvDataSourceImpl(csvDataSource)
    }

    @Test
    fun `should return all tasks when getAllTasks is called`() {
        // Given
        val task1 = createTask(
            projectId = "project-1",
            title = "Task 1",
            description = "Description 1",
            createdBy = "user-1",
            stateId = "state-1",
            createdAt = LocalDateTime(2023, 1, 1, 0, 0),
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )
        val task2 = createTask(
            projectId = "project-2",
            title = "Task 2",
            description = "Description 2",
            createdBy = "user-2",
            stateId = "state-2",
            createdAt = LocalDateTime(2023, 1, 1, 0, 0),
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )
        val tasks = listOf(task1, task2)
        every { csvDataSource.loadAllDataFromFile() } returns tasks

        // When
        val result = tasksCsvDataSource.getAllTasks()

        // Then
        assertThat(result).containsExactly(task1, task2).inOrder()
        verify { csvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should return empty list when no tasks exist`() {
        // Given
        every { csvDataSource.loadAllDataFromFile() } returns emptyList()

        // When
        val result = tasksCsvDataSource.getAllTasks()

        // Then
        assertThat(result).isEmpty()
        verify { csvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should return task when getTaskById is called with valid ID`() {
        // Given
        val task = createTask(
            projectId = "project-1",
            title = "Test Task",
            description = "Test Description",
            createdBy = "user-1",
            stateId = "state-1",
            createdAt = LocalDateTime(2023, 1, 1, 0, 0),
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )
        every { csvDataSource.loadAllDataFromFile() } returns listOf(task)

        // When
        val result = tasksCsvDataSource.getTaskById(task.id.toString())

        // Then
        assertThat(result).isEqualTo(task)
        verify { csvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should throw TaskNotFoundException when getTaskById is called with invalid ID`() {
        // Given
        every { csvDataSource.loadAllDataFromFile() } returns emptyList()
        val invalidTaskId = kotlin.uuid.Uuid.random().toString()

        // When / Then
        assertFailsWith<TaskNotFoundException> {
            tasksCsvDataSource.getTaskById(invalidTaskId)
        }

        verify { csvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should update task and call updateFile when updateTask is called with valid ID`() {
        // Given
        val originalTask = createTask( title = "Original Task")
        val updatedTask = originalTask.copy(title = "Updated Task")
        every { csvDataSource.loadAllDataFromFile() } returns listOf(originalTask)
        every { csvDataSource.updateFile(any()) } returns Unit

        // When
        val result = tasksCsvDataSource.updateTask( updatedTask)

        // Then
        assertThat(result).isEqualTo(updatedTask)
        verify { csvDataSource.loadAllDataFromFile() }
        verify { csvDataSource.updateFile(listOf(updatedTask)) }
    }

    @Test
    fun `should throw TaskNotFoundException when updateTask is called with invalid ID`() {
        // Given
        val updatedTask = createTask(
            projectId = "project-1",
            title = "Updated Task",
            description = "Updated Description",
            createdBy = "user-1",
            stateId = "state-1",
            createdAt = LocalDateTime(2023, 1, 1, 0, 0),
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )
        val taskId = updatedTask.id.toString()
        every { csvDataSource.loadAllDataFromFile() } returns emptyList()

        // When
        val exception = assertFailsWith<TaskNotFoundException> {
            tasksCsvDataSource.updateTask(updatedTask)
        }

        // Then
        assertThat(exception.message).isEqualTo("Task with ID $taskId not found")
        verify { csvDataSource.loadAllDataFromFile() }
        verify(exactly = 0) { csvDataSource.updateFile(any()) }
    }

    private val task1 = createTask(title = "First Task")
    private val task2 = createTask(title = "Second Task")

    @Test
    fun `addTask should append task to data source`() {
        // When
        tasksCsvDataSource.addTask(task1)

        // Then
        verify { csvDataSource.appendToFile(task1) }
    }

    @Test
    fun `deleteTask should remove task by id`() {
        // Given
        every { csvDataSource.loadAllDataFromFile() } returns listOf(task1, task2)

        // When
        tasksCsvDataSource.deleteTask(task1.id.toString())

        // Then
        verify { csvDataSource.deleteById(task1.id.toString()) }
    }
}