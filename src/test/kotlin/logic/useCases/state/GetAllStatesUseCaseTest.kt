package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.every
import io.mockk.mockk
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi

class GetAllStatesUseCaseTest {
    private lateinit var statesRepository: StatesRepository
    private lateinit var getAllState: GetAllStatesUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        getAllState = GetAllStatesUseCase(statesRepository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `get all states test`() {
        //Given
        val projects = listOf(
            createProject(
                name = "PlanMate Core Features",
                createdBy = "adminUser01"
            ),
            createProject(
                name = "PlanMate Core Features",
                createdBy = "adminUser01"
            )
        )

        // when
        val states = listOf(
            createState(
                id = "1",
                name = "in progress",
                projectId = projects[0].id.toString()
            ),
            createState(
                id = "2",
                name = "Done",
                projectId = projects[1].id.toString()
            )
        )

        every { statesRepository.getAllStates() } returns states

        //Then
        assertThat(getAllState.getAllStates()).isEqualTo(states)
    }

}