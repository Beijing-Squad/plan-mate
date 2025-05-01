package data.csvDataSource

import data.csvDataSource.csv.CsvDataSourceImpl
import fake.createTask
import io.mockk.*
import logic.entities.Task
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class TasksCsvDataSourceImplTest {

    private lateinit var csvDataSource: CsvDataSourceImpl<Task>
    private lateinit var tasksDataSourceImpl: TasksCsvDataSourceImpl

    private val task1 = createTask(title = "First Task")
    private val task2 = createTask(title = "Second Task")

    @BeforeEach
    fun setup() {
        csvDataSource = mockk(relaxed = true)
        tasksDataSourceImpl = TasksCsvDataSourceImpl(csvDataSource)
    }

    @Test
    fun `addTask should append task to data source`() {
        // When
        tasksDataSourceImpl.addTask(task1)

        // Then
        verify { csvDataSource.appendToFile(task1) }
    }

    @Test
    fun `deleteTask should remove task by id`() {
        // Given
        every { csvDataSource.loadAllDataFromFile() } returns listOf(task1, task2)

        // When
        tasksDataSourceImpl.deleteTask(task1.id.toString())

        // Then
        verify { csvDataSource.deleteById(task1.id.toString()) }
    }
}
