package data.csvDataSource

import com.google.common.truth.Truth.assertThat
import data.local.csvDataSource.StatesCsvDataSourceImpl
import data.local.csvDataSource.csv.CsvDataSourceImpl
import fake.createState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.TaskState
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TaskStatesCsvDataSourceImplTest {

    private lateinit var csvDataSource: CsvDataSourceImpl<TaskState>
    private lateinit var statesCsvDataSource: TaskStatesCsvDataSourceImpl

    @BeforeEach
    fun setup() {
        csvDataSource = mockk(relaxed = true)
        every { csvDataSource.loadAllDataFromFile() } returns mutableListOf()
        statesCsvDataSource = TaskStatesCsvDataSourceImpl(csvDataSource)
    }

    @Test
    fun `should add a new state to the data source`() {
        // Given
        val newState = createState(id = "1", name = "InProgress", projectId = "project1")

        // When
        val result = statesCsvDataSource.addState(newState)

        // Then
        assertThat(result).isTrue()
        verify { csvDataSource.updateFile(match { it.contains(newState) }) }
    }

    @Test
    fun `should return states filtered by project id`() {
        // Given
        val state1 = createState(id = "1", name = "Todo", projectId = "p1")
        val state2 = createState(id = "2", name = "Done", projectId = "p2")
        every { csvDataSource.loadAllDataFromFile() } returns listOf(state1, state2)

        // When
        val result = statesCsvDataSource.getStatesByProjectId("p1")

        // Then
        assertThat(result).containsExactly(state1)
    }

    @Test
    fun `should return state by id if it exists`() {
        // Given
        val state = createState(id = "1", name = "InProgress", projectId = "project1")
        every { csvDataSource.loadAllDataFromFile() } returns listOf(state)

        // When
        val result = statesCsvDataSource.getStateById("1")

        // Then
        assertThat(result).isEqualTo(state)
    }

    @Test
    fun `should delete an existing state`() {
        // Given
        val state = createState(id = "1", name = "Todo", projectId = "p1")
        val stateList = mutableListOf(state)
        every { csvDataSource.loadAllDataFromFile() } returns stateList

        statesCsvDataSource = TaskStatesCsvDataSourceImpl(csvDataSource)

        // When
        val result = statesCsvDataSource.deleteState(state)

        // Then
        assertEquals(true, result)
    }
}
