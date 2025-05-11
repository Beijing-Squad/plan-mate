package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.MateUnauthorizedException
import logic.exceptions.StateNotFoundException
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GetTaskStatesByProjectIdUseCaseTest {

    private lateinit var getStatesByProjectIdUseCase: GetStatesByProjectIdUseCase
    private lateinit var stateRepository: StatesRepository


    @BeforeEach
    fun setUp() {
        stateRepository = mockk(relaxed = true)
        getStatesByProjectIdUseCase = GetStatesByProjectIdUseCase(stateRepository)
    }


    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return states of project when are exist`() {
        runTest {
            // Given
            val projectId = Uuid.random().toString()
            val states = listOf(
                createState(
                    projectId = projectId
                ),
                createState(
                    projectId = projectId
                )
            )
            coEvery { stateRepository.getStatesByProjectId(projectId) } returns states
            // When
            val result = getStatesByProjectIdUseCase.getStatesByProjectId(projectId)
            // Then
            result.forEach { state ->
                assertThat(state.projectId).isEqualTo(Uuid.parse(projectId))
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when not found states with the project id`() {
        runTest {
            // Given
            val projectId = createProject().id.toString()
            val states = listOf(
                createState(),
                createState()
            )

            coEvery { stateRepository.getAllStates() } returns states
            coEvery { stateRepository.getStatesByProjectId(projectId) } returns emptyList()

            // When & Then
            assertThrows<StateNotFoundException> {
                getStatesByProjectIdUseCase.getStatesByProjectId(projectId)
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when user is not admin`() {
        runTest {
            // Given
            val projectId = createProject().id.toString()

            coEvery { stateRepository.getStatesByProjectId(projectId) } throws MateUnauthorizedException()

            // When && Then
            assertThrows<MateUnauthorizedException> {
                getStatesByProjectIdUseCase.getStatesByProjectId(projectId)
            }
        }
    }
}