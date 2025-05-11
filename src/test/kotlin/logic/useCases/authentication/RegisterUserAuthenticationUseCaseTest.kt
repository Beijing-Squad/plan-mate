package logic.useCases.authentication

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.type.UserRole
import logic.exceptions.InvalidPasswordException
import logic.exceptions.InvalidUserNameException
import logic.exceptions.UserExistsException
import logic.repository.AuthenticationRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RegisterUserAuthenticationUseCaseTest {

    private val repository = mockk<AuthenticationRepository>()
    private lateinit var registerUseCase: RegisterUserAuthenticationUseCase

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        registerUseCase = RegisterUserAuthenticationUseCase(repository)
    }

    @Test
    fun `execute should return true when registration is successful`() {
        runTest {
            // Given
            coEvery {
                repository
                    .registerUser("newuser", "password123", UserRole.MATE)
            } returns true

            // When
            val result = registerUseCase
                .execute("newuser", "password123", UserRole.MATE)

            // Then
            assertThat(result).isTrue()
            coVerify(exactly = 1) {
                repository
                    .registerUser("newuser", "password123", UserRole.MATE)
            }
        }
    }

    @Test
    fun `execute should return false when registration fails`() {
        runTest {
            // Given
            coEvery {
                repository
                    .registerUser("existinguser", "password123", UserRole.MATE)
            } returns false

            // When
            val result = registerUseCase
                .execute("existinguser", "password123", UserRole.MATE)

            // Then
            assertThat(result).isFalse()
            coVerify(exactly = 1) {
                repository
                    .registerUser("existinguser", "password123", UserRole.MATE)
            }
        }
    }

    @Test
    fun `execute should handle admin role registration correctly`() {
        runTest {
            // Given
            coEvery {
                repository
                    .registerUser("adminuser", "adminpass", UserRole.ADMIN)
            } returns true

            // When
            val result = registerUseCase
                .execute("adminuser", "adminpass", UserRole.ADMIN)

            // Then
            assertThat(result).isTrue()
            coVerify(exactly = 1) {
                repository
                    .registerUser("adminuser", "adminpass", UserRole.ADMIN)
            }
        }
    }

    @Test
    fun `execute should propagate exceptions from repository`() {
        runTest {
            // Given
            val exception = UserExistsException("User already exists")
            coEvery {
                repository
                    .registerUser("existinguser", "password123", UserRole.MATE)
            } throws exception

            // When/Then
            val thrownException = assertThrows<UserExistsException> {
                registerUseCase
                    .execute("existinguser", "password123", UserRole.MATE)
            }

            assertThat(thrownException).isEqualTo(exception)
            coVerify(exactly = 1) {
                repository
                    .registerUser("existinguser", "password123", UserRole.MATE)
            }
        }
    }

    @Test
    fun `execute should handle invalid username exception`() {
        runTest {
            // Given
            val exception = InvalidUserNameException("Username cannot be blank")
            coEvery {
                repository
                    .registerUser("", "password123", UserRole.MATE)
            } throws exception

            // When/Then
            val thrownException = assertThrows<InvalidUserNameException> {
                registerUseCase
                    .execute("", "password123", UserRole.MATE)
            }

            assertThat(thrownException).isEqualTo(exception)
            coVerify(exactly = 1) {
                repository
                    .registerUser("", "password123", UserRole.MATE)
            }
        }
    }

    @Test
    fun `execute should handle invalid password exception`() {
        runTest {
            // Given
            val exception = InvalidPasswordException("Password cannot be blank")
            coEvery {
                repository
                    .registerUser("username", "", UserRole.MATE)
            } throws exception

            // When/Then
            val thrownException = assertThrows<InvalidPasswordException> {
                registerUseCase
                    .execute("username", "", UserRole.MATE)
            }

            assertThat(thrownException).isEqualTo(exception)
            coVerify(exactly = 1) {
                repository
                    .registerUser("username", "", UserRole.MATE)
            }
        }
    }
}