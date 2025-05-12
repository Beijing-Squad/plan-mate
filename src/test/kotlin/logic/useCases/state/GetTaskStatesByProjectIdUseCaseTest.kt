package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.ProjectNotFoundException
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GetTaskStatesByProjectIdUseCaseTest {

    private lateinit var getTaskStatesByProjectIdUseCase: GetTaskStatesByProjectIdUseCase
    private lateinit var stateRepository: StatesRepository


    @BeforeEach
    fun setUp() {
        stateRepository = mockk(relaxed = true)
        getTaskStatesByProjectIdUseCase = GetTaskStatesByProjectIdUseCase(stateRepository)
    }

    @Test
    fun `should return states of project when are exist`() {
        runTest {
            // Given
            val projectId = Uuid.random()
            val states = listOf(
                createState(
                    projectId = projectId
                ),
                createState(
                    projectId = projectId
                )
            )
            coEvery { stateRepository.getTaskStatesByProjectId(projectId) } returns states
            // When
            val result = getTaskStatesByProjectIdUseCase.getTaskStatesByProjectId(projectId)
            // Then
            result.forEach { state ->
                assertThat(state.projectId).isEqualTo(projectId)
            }
        }
    }

    @Test
    fun `should throw exception when not found states with the project id`() {
        runTest {
            // Given
            val projectId = createProject().id.toString()
            val states = listOf(
                createState(),
                createState()
            )

            coEvery { stateRepository.getAllTaskStates() } returns states
            coEvery { stateRepository.getTaskStatesByProjectId(Uuid.parse(projectId)) } throws ProjectNotFoundException()

            // When & Then
            assertThrows<ProjectNotFoundException> {
                getTaskStatesByProjectIdUseCase.getTaskStatesByProjectId(Uuid.parse(projectId))
            }
        }
    }
}