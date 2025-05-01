package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createState
import io.mockk.every
import io.mockk.mockk
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class GetStateByIdUseCaseTest {
    private lateinit var statesRepository: StatesRepository
    private lateinit var getStateByIdUseCase: GetStateByIdUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk()
        getStateByIdUseCase = GetStateByIdUseCase(statesRepository)
    }

    @Test
    fun shouldReturnStateWhenIdExists() {
        // Given
        val expectedState = createState(id = "456", name = "Review", projectId = "project-2")

        every { statesRepository.getStateById("456") } returns expectedState

        // When
        val result = getStateByIdUseCase.getStateById(stateId = "456")

        // Then
        assertThat(result).isEqualTo(expectedState)
    }

    @Test
    fun shouldReturnNullWhenStateIdDoesNotExist() {
        // Given
        every { statesRepository.getStateById("999") } returns null

        // When
        val result = getStateByIdUseCase.getStateById("999")

        // Then
        assertThat(result).isNull()
    }

}