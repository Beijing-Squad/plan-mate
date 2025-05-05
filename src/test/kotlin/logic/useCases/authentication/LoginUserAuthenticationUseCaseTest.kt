package logic.useCases.authentication

import com.google.common.truth.Truth.assertThat
import fake.createUser
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.UserRole
import logic.entities.exceptions.InvalidUserNameException
import logic.entities.exceptions.UserNotFoundException
import logic.repository.AuthenticationRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LoginUserAuthenticationUseCaseTest {

    private val repository = mockk<AuthenticationRepository>()
    private val sessionManagerUseCase = mockk<SessionManagerUseCase>(relaxed = true)
    private lateinit var loginUseCase: LoginUserAuthenticationUseCase

    // Test data
    private val testUser = createUser(
        userName = "mohamed",
        password = "5f4dcc3b5aa765d61d8327deb882cf99", // hashed "password123"
        role = UserRole.ADMIN
    )

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        loginUseCase = LoginUserAuthenticationUseCase(repository, sessionManagerUseCase)
    }

    @Test
    fun `execute should return user when credentials are valid`() {
        // Given
        every { repository.loginUser("mohamed", "password123") } returns testUser

        // When
        val result = loginUseCase.execute("mohamed", "password123")

        // Then
        assertThat(result).isEqualTo(testUser)
        verify(exactly = 1) { repository.loginUser("mohamed", "password123") }
        verify(exactly = 1) { sessionManagerUseCase.setCurrentUser(testUser) }
    }

    @Test
    fun `execute should set current user in session when authentication is successful`() {
        // Given
        every { repository.loginUser("mohamed", "password123") } returns testUser

        // When
        loginUseCase.execute("mohamed", "password123")

        // Then
        verify(exactly = 1) { sessionManagerUseCase.setCurrentUser(testUser) }
    }

    @Test
    fun `execute should propagate exceptions from repository`() {
        // Given
        val exception = UserNotFoundException("Invalid username or password")
        every {
            repository
                .loginUser("wronguser", "wrongpass")
        } throws exception

        // When/Then
        val thrownException = assertThrows<UserNotFoundException> {
            loginUseCase
                .execute("wronguser", "wrongpass")
        }

        assertThat(thrownException).isEqualTo(exception)
        verify(exactly = 1) { repository.loginUser("wronguser", "wrongpass") }
        verify(exactly = 0) { sessionManagerUseCase.setCurrentUser(any()) }
    }

    @Test
    fun `execute should handle different user roles correctly`() {
        // Given
        val regularUser = createUser(
            userName = "regularuser",
            password = "5f4dcc3b5aa765d61d8327deb882cf99",
            role = UserRole.MATE
        )
        every {
            repository
                .loginUser("regularuser", "password123")
        } returns regularUser

        // When
        val result = loginUseCase
            .execute("regularuser", "password123")

        // Then
        assertThat(result).isEqualTo(regularUser)
        verify(exactly = 1) { sessionManagerUseCase.setCurrentUser(regularUser) }
    }

    @Test
    fun `execute should handle empty username and password`() {
        // Given
        every {
            repository
                .loginUser("", "")
        } throws InvalidUserNameException("Username cannot be blank")

        // When/Then
        assertThrows<InvalidUserNameException> {
            loginUseCase.execute("", "")
        }

        verify(exactly = 1) { repository.loginUser("", "") }
        verify(exactly = 0) { sessionManagerUseCase.setCurrentUser(any()) }
    }

    @Test
    fun `execute should handle whitespace username and password`() {
        // Given
        every {
            repository
                .loginUser("   ", "   ")
        } throws InvalidUserNameException("Username cannot be blank")

        // When/Then
        assertThrows<InvalidUserNameException> {
            loginUseCase.execute("   ", "   ")
        }

        verify(exactly = 1) { repository.loginUser("   ", "   ") }
        verify(exactly = 0) { sessionManagerUseCase.setCurrentUser(any()) }
    }
}