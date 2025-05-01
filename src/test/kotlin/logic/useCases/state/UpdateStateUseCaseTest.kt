package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.every
import io.mockk.mockk
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi

class UpdateStateUseCaseTest {
    private lateinit var statesRepository: StatesRepository
    private lateinit var updateStateUseCase: UpdateStateUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk()
        updateStateUseCase = UpdateStateUseCase(statesRepository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should update state when state is exist`() {
        //Given
        val project = createProject(
            name = "PlanMate Core Features",
            createdBy = "adminUser01"
        )
        val state = createState(
            name = "in progress",
            projectId = project.id.toString()
        )
        val states = listOf(state, state)
        val newState = createState(
            id = state.id,
            name = "done",
            projectId = state.projectId
        )

        every { statesRepository.updateState(newState) } returns true

        // when
        val result = updateStateUseCase.updateState(newState)

        //Then
        assertThat(result).isTrue()
    }
}