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
    private lateinit var validationUserUseCase: ValidationUserUseCase

    @BeforeEach
    fun setUp() {
        userRepository = mockk(relaxed = true)
        validationUserUseCase = mockk(relaxed = true)
        updateUser = UpdateUserUseCase(
            userRepository,
            validationUserUseCase
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

    @Test
    fun `should throw InvalidUserNameException when username is invalid`() {
        // Given
        val user = createUser(userName = EMPTY_STRING)
        every { validationUserUseCase.isUserNameBlank(any()) } returns true
        every { validationUserUseCase.isPasswordBlack(any()) } returns false

        // When && Then
        assertThrows<InvalidUserNameException> {
            updateUser.updateUser(user)
        }
    }

    @Test
    fun `should throw InvalidPasswordException when password is invalid`() {
        // Given
        val user = createUser(userName = "mohammed", password = EMPTY_STRING)
        every { validationUserUseCase.isUserNameBlank(any()) } returns false
        every { validationUserUseCase.isPasswordBlack(any()) } returns true

        // When && Then
        assertThrows<InvalidPasswordException> {
            updateUser.updateUser(user)
        }
    }

    private companion object{
        const val EMPTY_STRING = ""
    }
}