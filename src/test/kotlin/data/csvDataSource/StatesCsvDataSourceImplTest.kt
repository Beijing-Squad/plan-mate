package data.csvDataSource

import com.google.common.base.CharMatcher.any
import com.google.common.base.Verify.verify
import com.google.common.truth.Truth.assertThat
import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.StatesDataSource
import fake.createState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.BeforeEach
import kotlin.collections.first
import kotlin.test.Test
import kotlin.test.assertFailsWith

class StatesCsvDataSourceImplTest {
    private lateinit var statesCsvDataSourceImpl: StatesDataSource
    private lateinit var csvDataSourceImpl: CsvDataSourceImpl<State>
    private lateinit var testStates: List<State>

    @BeforeEach
    fun setUp() {
        csvDataSourceImpl = mockk()
        testStates = listOf(
            createState(id = "1", name = "To Do", projectId = "project1"),
            createState(id = "2", name = "In Progress", projectId = "project1"),
            createState(id = "3", name = "Done", projectId = "project2")
        )
        statesCsvDataSourceImpl = StatesCsvDataSourceImpl(csvDataSourceImpl)
    }

    // 1. `getAllStates`
    @Test
    fun `getAllStates should return all states from the data source`() {
        // Given
        every { csvDataSourceImpl.loadAllDataFromFile() } returns testStates

        // When
        val result = statesCsvDataSourceImpl.getAllStates()

        // Then
        assertThat(result).isEqualTo(testStates)
        verify { csvDataSourceImpl.loadAllDataFromFile() }
    }

    @Test
    fun `getAllStates should return empty list when no states exist`() {
        // Given
        every { csvDataSourceImpl.loadAllDataFromFile() } returns emptyList()

        // When
        val result = statesCsvDataSourceImpl.getAllStates()

        // Then
        assertThat(result).isEmpty()
    }

    // 2. `getStateById`
    @Test
    fun `getStateById should return state when state id exists`() {
        // Given
        val state = testStates.first()
        every { csvDataSourceImpl.loadAllDataFromFile() } returns testStates

        // When
        val result = statesCsvDataSourceImpl.getStateById(state.id)

        // Then
        assertThat(result).isEqualTo(state)
    }

    @Test
    fun `getStateById should throw exception when state id does not exist`() {
        // Given
        every { csvDataSourceImpl.loadAllDataFromFile() } returns testStates

        // When / Then
        assertFailsWith<StateNotFoundException> {
            statesCsvDataSourceImpl.getStateById("state not found")
        }
    }

    // 3. `getStatesByProjectId`
    @Test
    fun `getStatesByProjectId should return states by project id`() {
        // Given
        every { csvDataSourceImpl.loadAllDataFromFile() } returns testStates

        // When
        val result = statesCsvDataSourceImpl.getStatesByProjectId("project1")

        // Then
        assertEquals(2, result.size)
        for (state in result) {
            assertEquals("project1", state.projectId)
        }
    }

    @Test
    fun `getStatesByProjectId should return empty list when no states match project id`() {
        // Given
        every { csvDataSourceImpl.loadAllDataFromFile() } returns testStates

        // When
        val result = statesCsvDataSourceImpl.getStatesByProjectId("non-existent-project")

        // Then
        assertThat(result).isEmpty()
    }

    // 4. `addState`
    @Test
    fun `addState should return true when state is added successfully`() {
        // Given
        val newState = createState(id = "4", name = "Blocked", projectId = "project1")
        every { csvDataSourceImpl.loadAllDataFromFile() } returns testStates
        every { csvDataSourceImpl.updateFile(any()) }

        // When
        val result = statesCsvDataSourceImpl.addState(newState)

        // Then
        assertThat(result).isEqualTo(true)
        verify { csvDataSourceImpl.updateFile(any()) }
    }

    @Test
    fun `addState should throw exception when state id already exists`() {
        // Given
        val existingState = testStates.first()
        every { csvDataSourceImpl.loadAllDataFromFile() } returns testStates

        // When / Then
        val exception = assertFailsWith<StateAlreadyExistException> {
            statesCsvDataSourceImpl.addState(existingState)
        }

        // Then
        assertThat(exception.message).isEqualTo("State with id ${existingState.id} already exists")
    }

    // 5. `updateState`
    @Test
    fun `updateState should update existing state`() {
        // Given
        val updatedState = testStates[0].copy(name = "Updated To Do")
        every { csvDataSourceImpl.loadAllDataFromFile() } returns testStates
        every { csvDataSourceImpl.updateFile(any()) } returns true

        // When
        val result = statesCsvDataSourceImpl.updateState(updatedState)

        // Then
        assertThat(result).isEqualTo(updatedState)
        verify { csvDataSourceImpl.updateFile(any()) }
    }

    @Test
    fun `updateState should throw exception when state id does not exist`() {
        // Given
        val nonExistentState = createState(id = "999", name = "Non-existent State", projectId = "project1")
        every { csvDataSourceImpl.loadAllDataFromFile() } returns testStates

        // When / Then
        assertFailsWith<StateNotFoundException> {
            statesCsvDataSourceImpl.updateState(nonExistentState)
        }
    }

    // 6. `deleteState`
    @Test
    fun `deleteState should delete existing state`() {
        // Given
        val targetState = testStates.first()
        every { csvDataSourceImpl.loadAllDataFromFile() } returns testStates
        every { csvDataSourceImpl.updateFile(any()) }

        // When
        val result = statesCsvDataSourceImpl.deleteState(targetState)

        // Then
        assertThat(result).isEqualTo(true)
        verify { csvDataSourceImpl.updateFile(any()) }
    }
}