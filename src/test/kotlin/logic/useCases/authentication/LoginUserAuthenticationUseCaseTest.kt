package logic.useCases.authentication

import com.google.common.truth.Truth.assertThat
import fake.createUser
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.entity.type.UserRole
import logic.exceptions.InvalidUserNameException
import logic.exceptions.UserNotFoundException
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
        runTest {
            // Given
            coEvery { repository.loginUser("mohamed", "password123") } returns testUser

            // When
            val result = loginUseCase.execute("mohamed", "password123")

            // Then
            assertThat(result).isEqualTo(testUser)
            coVerify(exactly = 1) { repository.loginUser("mohamed", "password123") }
            verify(exactly = 1) { sessionManagerUseCase.setCurrentUser(testUser) }
        }
    }

    @Test
    fun `execute should set current user in session when authentication is successful`() {
        runTest {
            // Given
            coEvery { repository.loginUser("mohamed", "password123") } returns testUser

            // When
            loginUseCase.execute("mohamed", "password123")

            // Then
            verify(exactly = 1) { sessionManagerUseCase.setCurrentUser(testUser) }
        }
    }

    @Test
    fun `execute should propagate exceptions from repository`() {
        runTest {
            // Given
            val exception = UserNotFoundException("Invalid username or password")
            coEvery {
                repository
                    .loginUser("wronguser", "wrongpass")
            } throws exception

            // When/Then
            val thrownException = assertThrows<UserNotFoundException> {
                loginUseCase
                    .execute("wronguser", "wrongpass")
            }

            assertThat(thrownException).isEqualTo(exception)
            coVerify(exactly = 1) { repository.loginUser("wronguser", "wrongpass") }
            verify(exactly = 0) { sessionManagerUseCase.setCurrentUser(any()) }
        }
    }

    @Test
    fun `execute should handle different user roles correctly`() {
        runTest {
            // Given
            val regularUser = createUser(
                userName = "regularuser",
                password = "5f4dcc3b5aa765d61d8327deb882cf99",
                role = UserRole.MATE
            )
            coEvery {
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
    }

    @Test
    fun `execute should handle empty username and password`() {
        runTest {
            // Given
            coEvery {
                repository
                    .loginUser("", "")
            } throws InvalidUserNameException("Username cannot be blank")

            // When/Then
            assertThrows<InvalidUserNameException> {
                loginUseCase.execute("", "")
            }

            coVerify(exactly = 1) { repository.loginUser("", "") }
            verify(exactly = 0) { sessionManagerUseCase.setCurrentUser(any()) }
        }
    }

    @Test
    fun `execute should handle whitespace username and password`() {
        runTest {
            // Given
            coEvery {
                repository
                    .loginUser("   ", "   ")
            } throws InvalidUserNameException("Username cannot be blank")

            // When/Then
            assertThrows<InvalidUserNameException> {
                loginUseCase.execute("   ", "   ")
            }

            coVerify(exactly = 1) { repository.loginUser("   ", "   ") }
            verify(exactly = 0) { sessionManagerUseCase.setCurrentUser(any()) }
        }
    }
}