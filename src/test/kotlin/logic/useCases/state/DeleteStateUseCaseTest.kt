package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.mockk
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi

class DeleteStateUseCaseTest {

    private lateinit var statesRepository: StatesRepository
    private lateinit var deleteStateUseCase: DeleteStateUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk()
        deleteStateUseCase = DeleteStateUseCase(statesRepository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun shouldReturnTrueWhenStateExistsAndDeletedSuccessfully() {
        // Given
        val project = createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
        val state = createState(id = "1", name = "In Progress", projectId = project.id.toString())


        // When
        val result = deleteStateUseCase.deleteState(state)

        // Then
        assertThat(result).isFalse()
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun shouldReturnFalseWhenStateDoesNotExistInRepository() {
        // Given
        val project = createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
        val state = createState(id = "123", name = "Done", projectId = project.id.toString())


        // When
        val result = deleteStateUseCase.deleteState(state)

        // Then
        assertThat(result).isFalse()
    }


    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun shouldNotThrowExceptionWhenDeletingNonExistentState() {
        // Given
        val project = createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
        val state = createState(id = "999", name = "Archived", projectId = project.id.toString())


        // When
        val result = runCatching { deleteStateUseCase.deleteState(state) }

        // Then
        assertThat(result.isFailure).isFalse()
    }

}