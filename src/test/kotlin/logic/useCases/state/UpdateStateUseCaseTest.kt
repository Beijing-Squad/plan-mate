package logic.useCases.state

import fake.createProject
import fake.createState
import io.mockk.mockk
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
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
    fun `update new state test`() {
        //Given
        val project = createProject(
            name = "PlanMate Core Features",
            createdBy = "adminUser01"
        )
        val newState = createState(
            name = "Done",
            projectId = project.id.toString()
        )

        // when
//        every { statesRepository.updateState(newState) } returns true
        val result = updateStateUseCase.updateState(newState)

        //Then
        assertEquals(true, result)
    }

}