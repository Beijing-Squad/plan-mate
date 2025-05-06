package logic.useCases.user

import com.google.common.truth.Truth.assertThat
import fake.createUser
import io.mockk.every
import io.mockk.mockk
import logic.entities.exceptions.InvalidPasswordException
import logic.entities.exceptions.InvalidUserNameException
import logic.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
        // When
        every { userRepository.updateUser(mohammed) } returns userUpdated
        val actual = updateUser.updateUser(mohammed)
        // Then
        assertThat(actual).isEqualTo(userUpdated)
    }

    private companion object {
        const val EMPTY_STRING = ""
    }
}