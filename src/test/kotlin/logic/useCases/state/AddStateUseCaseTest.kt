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
import kotlin.uuid.Uuid

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
    fun `should return true when add valid new state`() {
        // Given
        val project = createProject(
            name = "PlanMate Core Features", createdBy = "adminUser01"
        )
        val newState = createState(
            name = "Done", projectId = project.id.toString()
        )

        // when
        every { statesRepository.addState(newState) } returns true
        val result = addStateUseCase.addState(newState)

        //Then
        assertThat(result).isTrue()
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return false when add invalid new state with empty name`() {
        // Given
        val project = createProject(
            name = "PlanMate Core Features", createdBy = "adminUser01"
        )
        val newState = createState(
            name = "", projectId = project.id.toString()
        )

        // when
        every { statesRepository.addState(newState) } returns false
        val result = addStateUseCase.addState(newState)

        // Then
        assertThat(result).isFalse()
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return false when add new state with not exist project id`() {
        //Given
        val project = listOf(
            createProject(
                name = "PlanMate Core Features", createdBy = "adminUser01"
            ), createProject(
                name = "PlanMate Core Features 2", createdBy = "adminUser02"
            )
        )
        val newState = createState(
            name = "Done", projectId = Uuid.random().toString()
        )


        // when
        every { statesRepository.addState(newState) } returns false
        addStateUseCase.addState(newState)

        //Then
        assertThat(newState.projectId).isNotIn(
            project.map { it.id.toString() }
        )
    }
}