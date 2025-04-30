package logic.useCases.user

import com.google.common.truth.Truth.assertThat
import data.repository.UserRepositoryImpl
import fake.createUser
import io.mockk.mockk
import logic.repository.UserRepository
import logic.useCases.user.cryptography.MD5PasswordEncryption
import logic.useCases.user.cryptography.PasswordEncryption
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.uuid.ExperimentalUuidApi

class UpdateUserUseCaseTest {
    private lateinit var updateUser: UpdateUserUseCase
    private lateinit var userRepository: UserRepository
    private lateinit var passwordEncryption: PasswordEncryption
    private lateinit var validateUser: ValidateUserUseCase

    @BeforeEach
    fun setUp() {
        userRepository = UserRepositoryImpl(mockk(relaxed = true))
        passwordEncryption = MD5PasswordEncryption()
        validateUser = ValidateUserUseCase()
        updateUser = UpdateUserUseCase(userRepository,passwordEncryption,validateUser)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should update user when username and password are valid`() {
        // Given
        val userName = "mohammed123"
        val password = "12345678"
        val mohammed = createUser(userName = userName,password = password)
        val userUpdated = mohammed.copy(userName = "mohammed2001")
        // When

        val actual = updateUser.updateUser(userUpdated)

        // Then
        assertThat(actual.isSuccess).isTrue()
    }

    @ParameterizedTest
    @CsvSource(
        "mohammed akkad",
        "1mohammed",
        "mohammed 123",
        "ab",
        "@mohammed1234",
        "mohammed@123"
    )
    fun `should throw InvalidUserNameException when username is invalid`(username: String) {
        // Given
        val user = createUser(userName = username)

        // When
        val actual = updateUser.updateUser(user)

        // Given
        assertThat(actual.isFailure).isTrue()

    }

    @Test
    fun `should throw InvalidPasswordException when password is invalid`() {
        // Given
        val user = createUser(password = "1234")

        // When
        val actual = updateUser.updateUser(user)

        // Given
        assertThat(actual.isFailure).isTrue()

    }

}