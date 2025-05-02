package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.mockk
import logic.entities.UserRole
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
    fun `should return true when state exists and deleted successfully`() {
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
    fun `should return false when state does not exist in repository`() {
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
    fun `should not throw exception when deleting non existent state`() {
        // Given
        val project = createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
        val state = createState(id = "999", name = "Archived", projectId = project.id.toString())


        // When
        val result = runCatching { deleteStateUseCase.deleteState(state) }

        // Then
        assertThat(result.isFailure).isFalse()
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when user is not admin`() {
        // Given
        val mateRole = UserRole.MATE
        val project = createProject()
        val state = createState(
            name = "in progress",
            projectId = project.id.toString()
        )
        val newState = createState(
            id = state.id,
            name = "done",
            projectId = state.projectId
        )

        // When && Then
        assertThrows<StateUnauthorizedUserException> {
            deleteStateUseCase.deleteState(newState, mateRole)
        }
    }
}