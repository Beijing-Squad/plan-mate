package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.every
import io.mockk.mockk
import logic.entities.exceptions.MateUnauthorizedException
import logic.entities.exceptions.StateNotFoundException
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

class GetStatesByProjectIdUseCaseTest {

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
        // Given
        val project = createProject()
        val projectId = project.id.toString()
        val states = listOf(
            createState(
                projectId = projectId
            ),
            createState(
                projectId = project.id.toString()
            )
        )
        every { stateRepository.getStatesByProjectId(projectId) } returns states
        // When
        val result = getStatesByProjectIdUseCase.getStatesByProjectId(projectId)
        // Then
        result.forEach { state ->
            assertThat(state.projectId).isEqualTo(projectId)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when not found states with the project id`() {
        // Given
        val projectId = createProject().id.toString()
        val states = listOf(
            createState(),
            createState()
        )

        every { stateRepository.getAllStates() } returns states
        every { stateRepository.getStatesByProjectId(projectId) } returns emptyList()

        // When & Then
        assertThrows<StateNotFoundException> {
            getStatesByProjectIdUseCase.getStatesByProjectId(projectId)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when user is not admin`() {
        // Given
        val projectId = createProject().id.toString()

        every { stateRepository.getStatesByProjectId(projectId) } throws MateUnauthorizedException()

        // When && Then
        assertThrows<MateUnauthorizedException> {
            getStatesByProjectIdUseCase.getStatesByProjectId(projectId)
        }
    }
}