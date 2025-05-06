package data.csvDataSource

import com.google.common.truth.Truth.assertThat
import data.csvDataSource.csv.CsvDataSourceImpl
import fake.createState
import io.mockk.*
import logic.entities.State
import logic.entities.exceptions.CsvWriteException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class TaskStatesCsvDataSourceImplTest {

    private lateinit var csvDataSource: CsvDataSourceImpl<State>
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
    fun `should update an existing state`() {
        // Given
        val oldState = createState(id = "1", name = "Todo", projectId = "p1")
        val stateList = mutableListOf(oldState)
        every { csvDataSource.loadAllDataFromFile() } returns stateList
        statesCsvDataSource = TaskStatesCsvDataSourceImpl(csvDataSource)
        every { csvDataSource.updateFile(any()) } just Runs

        val updatedState = oldState.copy(name = "InProgress", projectId = "p2")

        // When
        val result = statesCsvDataSource.updateState(updatedState)

        // Then
        assertThat(result).isEqualTo(updatedState)
        verify { csvDataSource.updateFile(match { it.contains(updatedState) }) }
    }

    @Test
    fun `should throw exception when update state fails to write to file`() {
        // Given
        val originalState = createState(id = "1", name = "Todo", projectId = "p1")
        val updatedState = originalState.copy(name = "InProgress")
        val stateList = mutableListOf(originalState)
        every { csvDataSource.loadAllDataFromFile() } returns stateList
        statesCsvDataSource = TaskStatesCsvDataSourceImpl(csvDataSource)
        every { csvDataSource.updateFile(any()) } throws CsvWriteException("Failed")

        // When & Then
        assertThrows<CsvWriteException> {
            statesCsvDataSource.updateState(updatedState)
        }
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
