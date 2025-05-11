package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class DeleteTaskStateUseCaseTest {

    private lateinit var statesRepository: StatesRepository
    private lateinit var deleteTaskStateUseCase: DeleteTaskStateUseCase
    private lateinit var getTaskStateByIdUseCase: GetTaskStateByIdUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        getTaskStateByIdUseCase = GetTaskStateByIdUseCase(statesRepository)
        deleteTaskStateUseCase = DeleteTaskStateUseCase(statesRepository)
    }

    @Test
    fun `should return true when state exists and deleted successfully`() {
        runTest {
            // Given
            val fixedId = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
            val project = createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
            val state = createState(id = fixedId, name = "In Progress", projectId = project.id)
            coEvery { statesRepository.deleteTaskState(state.id) } returns true

            // When
            val result = deleteTaskStateUseCase.deleteTaskState(state.id)

            // Then
            assertThat(result).isTrue()
        }

    }

    @Test
    fun `should return false when try to delete non existent state`() {
        runTest {
            // Given
            val project = createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
            val state = createState(name = "Archived", projectId = project.id)

            coEvery { statesRepository.deleteTaskState(state.id) } returns false

            // When
            val result = deleteTaskStateUseCase.deleteTaskState(state.id)

            // Then
            assertThat(result).isFalse()
        }
    }
}