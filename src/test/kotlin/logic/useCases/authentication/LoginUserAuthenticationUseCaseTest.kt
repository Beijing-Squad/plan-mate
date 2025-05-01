package logic.useCases.authentication

import com.google.common.truth.Truth.assertThat
import fake.createUser
import io.mockk.every
import io.mockk.mockk
import logic.entities.exceptions.InvalidPasswordException
import logic.entities.exceptions.InvalidUserNameException
import logic.repository.AuthenticationRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LoginUserAuthenticationUseCaseTest {

    private lateinit var repository: AuthenticationRepository
    private lateinit var loginUserAuthenticationUseCase: LoginUserAuthenticationUseCase

    private val testUsername = "john_doe"
    private val testPassword = "secure123"

    @BeforeEach
    fun setUp() {
        repository = mockk()
        loginUserAuthenticationUseCase = LoginUserAuthenticationUseCase(repository)
    }

    @Test
    fun `given blank username, when execute called, then throw InvalidUserNameException`() {
        // Given
        val blankUsername = "  "

        // When / Then
        val exception = assertThrows<InvalidUserNameException> {
            loginUserAuthenticationUseCase.execute(blankUsername, testPassword)
        }
        assertThat(exception.message).isEqualTo("Invalid username")
    }

    @Test
    fun `given blank password, when execute called, then throw InvalidPasswordException`() {
        // Given
        val blankPassword = ""

        // When / Then
        val exception = assertThrows<InvalidPasswordException> {
            loginUserAuthenticationUseCase.execute(testUsername, blankPassword)
        }
        assertThat(exception.message).isEqualTo("Invalid password")
    }

    @Test
    fun `given username not found, when execute called, then throw InvalidUserNameException`() {
        // Given
        every { repository.loginUser(testUsername, testPassword) } returns null

        // When / Then
        val exception = assertThrows<InvalidUserNameException> {
            loginUserAuthenticationUseCase.execute(testUsername, testPassword)
        }
        assertThat(exception.message).isEqualTo("Invalid username")
    }

    @Test
    fun `given wrong password, when execute called, then throw InvalidPasswordException`() {
        // Given
        val wrongPassword = "wrongPass"
        val storedUser = createUser(userName = testUsername, password = "someOtherPassword")
        every { repository.loginUser(testUsername, wrongPassword) } returns storedUser

        // When / Then
        val exception = assertThrows<InvalidPasswordException> {
            loginUserAuthenticationUseCase.execute(testUsername, wrongPassword)
        }
        assertThat(exception.message).isEqualTo("Invalid password")
    }
}