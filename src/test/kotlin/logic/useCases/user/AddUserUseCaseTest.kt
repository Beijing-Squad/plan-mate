package logic.useCases.user

import com.google.common.truth.Truth.assertThat
import data.repository.UserRepositoryImpl
import fake.createUser
import io.mockk.mockk
import logic.entities.exceptions.InvalidPasswordException
import logic.entities.exceptions.InvalidUserNameException
import logic.repository.UserRepository
import logic.useCases.user.cryptography.MD5PasswordEncryption
import logic.useCases.user.cryptography.PasswordEncryption
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AddUserUseCaseTest {

    private lateinit var addUser: AddUserUseCase
    private lateinit var userRepository: UserRepository
    private lateinit var passwordEncryption: PasswordEncryption

    @BeforeEach
    fun setUp() {
        userRepository = UserRepositoryImpl(mockk(relaxed = true))
        passwordEncryption = MD5PasswordEncryption()
        addUser = AddUserUseCase(userRepository,passwordEncryption)
    }

    @Test
    fun `should create user when username and password are valid`() {
        // Given
        val userName = "mohammed123"
        val password = "12345678"
        val mohammed = createUser(userName = userName,password = password)

        // When
        addUser.addUser(mohammed)

        // Then
        assertThat(true).isTrue()
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

        // When && Given
        assertThrows<InvalidUserNameException> { addUser.addUser(user).getOrThrow() }

    }

    @Test
    fun `should throw InvalidPasswordException when password is invalid`() {
        // Given
        val user = createUser(password = "1234")

        // When && Given
        assertThrows<InvalidPasswordException> { addUser.addUser(user).getOrThrow() }

    }

}