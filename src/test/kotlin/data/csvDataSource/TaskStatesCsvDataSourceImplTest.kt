package data.csvDataSource

import com.google.common.truth.Truth.assertThat
import data.local.csvDataSource.TaskStatesCsvDataSourceImpl
import data.local.csvDataSource.csv.CsvDataSourceImpl
import fake.createProject
import fake.createState
import io.mockk.every
import io.mockk.mockk
import logic.entities.TaskState
import logic.exceptions.StateNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
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
        val project = listOf(
            createProject(
                id = Uuid.parse("964801c9-49f6-4e7b-899b-113337a91848"),
                name = "PlanMate Core Features",
                createdBy = "adminUser01"
            )
        )
        val newState = createState(
            id = Uuid.parse("ae7095f0-4a95-466b-8ed6-6f54827268da"),
            name = "InProgress",
            projectId = project[0].id
        )

        csvDataSource.appendToFile(newState)
        // When
        val result = statesCsvDataSource.addState(newState)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return states filtered by project id`() {
        // Given
        val project = listOf(
            createProject(
                id = Uuid.parse("964801c9-49f6-4e7b-899b-113337a91848"),
                name = "PlanMate Core Features",
                createdBy = "adminUser01"
            )
        )
        val state1 = createState(
            id = Uuid.parse("ae7095f0-4a95-466b-8ed6-6f54827268db"),
            name = "Todo",
            projectId = project[0].id
        )
        val state2 = createState(
            id = Uuid.parse("be7095f0-4a95-466b-8ed6-6f54827268db"),
            name = "Done",
            projectId = project[0].id
        )
        every { csvDataSource.loadAllDataFromFile() } returns listOf(state1, state2)

        // When
        val result = statesCsvDataSource.getStatesByProjectId("964801c9-49f6-4e7b-899b-113337a91848")

        // Then
        assertThat(result).containsExactly(state1, state2)
    }

    @Test
    fun `should return state by id if it exists`() {
        // Given
        val project = listOf(
            createProject(
                id = Uuid.parse("ae7095f0-4a95-466b-8ed6-6f54827268da"),
                name = "PlanMate Core Features",
                createdBy = "adminUser01"
            )
        )
        val state = createState(
            id = Uuid.parse("964801c9-49f6-4e7b-899b-113337a91848"),
            name = "InProgress",
            projectId = project[0].id
        )
        every { csvDataSource.loadAllDataFromFile() } returns listOf(state)

        // When
        val result = statesCsvDataSource.getStateById("964801c9-49f6-4e7b-899b-113337a91848")

        // Then
        assertThat(result).isEqualTo(state)
    }

    @Test
    fun `should throw exception when state id does not exist`() {
        // Given
        every { csvDataSource.loadAllDataFromFile() } returns emptyList()

        // When & Then
        assertThrows<StateNotFoundException> {
            statesCsvDataSource.getStateById("non-existing-id")
        }
    }

    @Test
    fun `should throw exception when state id does not exist in non-empty data source`() {
        // Given
        val state = createState(
            id = Uuid.parse("12345678-1234-1234-1234-123456789012"),
            name = "Done",
            projectId = Uuid.random()
        )
        every { csvDataSource.loadAllDataFromFile() } returns listOf(state)

        // When & Then
        val exception = kotlin.test.assertFailsWith<StateNotFoundException> {
            statesCsvDataSource.getStateById("non-existing-id")
        }
        assertThat(exception).isInstanceOf(StateNotFoundException::class.java)
    }

    @Test
    fun `should delete an existing state`() {
        // Given
        val project = listOf(
            createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
        )
        val state = createState(name = "Todo", projectId = project[0].id)
        val stateList = mutableListOf(state)
        every { csvDataSource.loadAllDataFromFile() } returns stateList

        statesCsvDataSource = TaskStatesCsvDataSourceImpl(csvDataSource)

        // When
        val result = statesCsvDataSource.deleteState(state)

        // Then
        assertEquals(true, result)
    }

    @Test
    fun `should update an existing state`() {
        // Given
        val project = createProject(
            id = Uuid.parse("964801c9-49f6-4e7b-899b-113337a91848"),
            name = "PlanMate Core Features",
            createdBy = "adminUser01"
        )
        val existingState = createState(
            id = Uuid.parse("ae7095f0-4a95-466b-8ed6-6f54827268da"),
            name = "ToDo",
            projectId = project.id
        )
        val updatedState = existingState.copy(name = "InProgress")
        every { csvDataSource.loadAllDataFromFile() } returns listOf(existingState)
        every { csvDataSource.updateFile(any()) } returns Unit

        // When
        val result = statesCsvDataSource.updateState(updatedState)

        // Then
        assertThat(result).isEqualTo(updatedState)
    }

    @Test
    fun `should throw exception when updating a non-existent state`() {
        // Given
        val nonExistentState = createState(
            id = Uuid.parse("123e4567-e89b-12d3-a456-426614174000"),
            name = "Done",
            projectId = Uuid.random()
        )
        every { csvDataSource.loadAllDataFromFile() } returns emptyList()

        // When & Then
        assertThrows<StateNotFoundException> {
            statesCsvDataSource.updateState(nonExistentState)
        }
    }
}
