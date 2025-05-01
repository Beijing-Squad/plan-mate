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
    fun `delete state test`() {
        //Given
        val adminRole = UserRole.ADMIN
        val project = createProject(
            name = "PlanMate Core Features",
            createdBy = "adminUser01"
        )
        val state = createState(
            name = "Done",
            projectId = project.id.toString()
        )

        // when
        val result = deleteStateUseCase.deleteState(state, adminRole)

        //Then
        assertThat(result).isFalse()
    }
}