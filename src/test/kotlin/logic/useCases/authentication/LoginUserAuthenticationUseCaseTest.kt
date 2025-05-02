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
    private lateinit var mD5PasswordUseCase: MD5PasswordUseCase
    private lateinit var sessionManager: SessionManager

    private val testUsername = "john_doe"
    private val testPassword = "secure123"

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        mD5PasswordUseCase = mockk(relaxed = true)
        sessionManager = mockk(relaxed = true)
        loginUserAuthenticationUseCase = LoginUserAuthenticationUseCase(repository,mD5PasswordUseCase,sessionManager)
    }

    @Test
    fun `given blank username, when execute called, then throw InvalidUserNameException`() {
        // Given
        val blankUsername = "  "

        // When / Then
        val exception = assertThrows<InvalidUserNameException> {
            loginUserAuthenticationUseCase.execute(blankUsername, testPassword)
        }
        assertThat(exception.message).isEqualTo(USERNAME_ERROR)
    }

    @Test
    fun `given blank password, when execute called, then throw InvalidPasswordException`() {
        // Given
        val blankPassword = ""

        // When / Then
        val exception = assertThrows<InvalidPasswordException> {
            loginUserAuthenticationUseCase.execute(testUsername, blankPassword)
        }
        assertThat(exception.message).isEqualTo(PASSWORD_ERROR)
    }

    @Test
    fun `given username not found, when execute called, then throw InvalidUserNameException`() {
        // Given
        every { repository.loginUser(testUsername, testPassword) } returns null

        // When / Then
        val exception = assertThrows<InvalidUserNameException> {
            loginUserAuthenticationUseCase.execute(testUsername, testPassword)
        }
        assertThat(exception.message).isEqualTo(USERNAME_ERROR)
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
        assertThat(exception.message).isEqualTo(PASSWORD_ERROR)
    }

    @Test
    fun `given valid username and password, when execute called, then return User`() {
        // Given
        val hashedPassword = "hashed_secure123"
        val expectedUser = createUser(userName = testUsername, password = hashedPassword)

        every { repository.loginUser(testUsername, testPassword) } returns expectedUser
        every { mD5PasswordUseCase.hashPassword(testPassword) } returns hashedPassword

        // When
        val result = loginUserAuthenticationUseCase.execute(testUsername, testPassword)

        // Then
        assertThat(result).isEqualTo(expectedUser)
    }

    private companion object {
        const val USERNAME_ERROR = "Invalid username"
        const val PASSWORD_ERROR = "Invalid password"
    }
}