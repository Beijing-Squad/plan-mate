package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.mockk
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi

class AddStateUseCaseTest {
    private lateinit var statesRepository: StatesRepository
    private lateinit var addStateUseCase: AddStateUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk()
        addStateUseCase = AddStateUseCase(statesRepository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `add new state test`() {
        //Given
        val project = createProject(
            name = "PlanMate Core Features",
            createdBy = "adminUser01"
        )
        val newState = createState(
            id = "123",
            name = "Done",
            projectId = project.id.toString()
        )

        // when
        val result = addStateUseCase.addState(newState)

        //Then
        assertThat(result).isFalse()
    }

}