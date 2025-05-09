package logic.useCases.user

import com.google.common.truth.Truth.assertThat
import fake.createUser
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi

class UpdateUserUseCaseTest {
    private lateinit var updateUser: UpdateUserUseCase
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository = mockk(relaxed = true)
        updateUser = UpdateUserUseCase(
            userRepository
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should update user when username and password are valid`() {
        // Given
        val userName = "mohammed123"
        val password = "12345678"
        val mohammed = createUser(userName = userName, password = password)
        val userUpdated = mohammed.copy(userName = "mohammed2001")
        // When && Then
        coEvery { userRepository.updateUser(mohammed) } returns userUpdated
        runTest {
            val actual = updateUser.updateUser(mohammed)
            assertThat(actual).isEqualTo(userUpdated)
        }
    }
}