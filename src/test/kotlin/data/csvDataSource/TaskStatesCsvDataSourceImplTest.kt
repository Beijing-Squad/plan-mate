package data.csvDataSource

import com.google.common.truth.Truth.assertThat
import data.local.csvDataSource.TaskStatesCsvDataSourceImpl
import data.local.csvDataSource.csv.CsvDataSourceImpl
import fake.createProject
import fake.createState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.TaskState
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi

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

    @Disabled
    @Test
    fun `should add a new state to the data source`() {
        // Given
        val project = listOf(
            createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
        )
        val newState = createState(name = "InProgress", projectId = project[0].id)

        // When
        val result = statesCsvDataSource.addState(newState)

        // Then
        assertThat(result).isTrue()
        verify { csvDataSource.updateFile(match { it.contains(newState) }) }
    }

    @Disabled
    @Test
    fun `should return states filtered by project id`() {
        // Given
        val project = listOf(
            createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
        )
        val state1 = createState(name = "Todo", projectId = project[0].id)
        val state2 = createState(name = "Done", projectId = project[0].id)
        every { csvDataSource.loadAllDataFromFile() } returns listOf(state1, state2)

        // When
        val result = statesCsvDataSource.getStatesByProjectId("p1")

        // Then
        assertThat(result).containsExactly(state1)
    }

    @Disabled
    @Test
    fun `should return state by id if it exists`() {
        // Given
        val project = listOf(
            createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
        )
        val state = createState(name = "InProgress", projectId = project[0].id)
        every { csvDataSource.loadAllDataFromFile() } returns listOf(state)

        // When
        val result = statesCsvDataSource.getStateById("1")

        // Then
        assertThat(result).isEqualTo(state)
    }

    @Disabled
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
}
