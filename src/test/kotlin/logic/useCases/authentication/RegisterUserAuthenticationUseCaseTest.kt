package logic.useCases.authentication

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.UserRole
import logic.entities.exceptions.InvalidPasswordException
import logic.entities.exceptions.InvalidUserNameException
import logic.entities.exceptions.UserExistsException
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
        // Given
        every {
            repository
                .registerUser("newuser", "password123", UserRole.MATE)
        } returns true

        // When
        val result = registerUseCase
            .execute("newuser", "password123", UserRole.MATE)

        // Then
        assertThat(result).isTrue()
        verify(exactly = 1) {
            repository
                .registerUser("newuser", "password123", UserRole.MATE)
        }
    }

    @Test
    fun `execute should return false when registration fails`() {
        // Given
        every {
            repository
                .registerUser("existinguser", "password123", UserRole.MATE)
        } returns false

        // When
        val result = registerUseCase
            .execute("existinguser", "password123", UserRole.MATE)

        // Then
        assertThat(result).isFalse()
        verify(exactly = 1) {
            repository
                .registerUser("existinguser", "password123", UserRole.MATE)
        }
    }

    @Test
    fun `execute should handle admin role registration correctly`() {
        // Given
        every {
            repository
                .registerUser("adminuser", "adminpass", UserRole.ADMIN)
        } returns true

        // When
        val result = registerUseCase
            .execute("adminuser", "adminpass", UserRole.ADMIN)

        // Then
        assertThat(result).isTrue()
        verify(exactly = 1) {
            repository
                .registerUser("adminuser", "adminpass", UserRole.ADMIN)
        }
    }

    @Test
    fun `execute should propagate exceptions from repository`() {
        // Given
        val exception = UserExistsException("User already exists")
        every {
            repository
                .registerUser("existinguser", "password123", UserRole.MATE)
        } throws exception

        // When/Then
        val thrownException = assertThrows<UserExistsException> {
            registerUseCase
                .execute("existinguser", "password123", UserRole.MATE)
        }

        assertThat(thrownException).isEqualTo(exception)
        verify(exactly = 1) {
            repository
                .registerUser("existinguser", "password123", UserRole.MATE)
        }
    }

    @Test
    fun `execute should handle invalid username exception`() {
        // Given
        val exception = InvalidUserNameException("Username cannot be blank")
        every {
            repository
                .registerUser("", "password123", UserRole.MATE)
        } throws exception

        // When/Then
        val thrownException = assertThrows<InvalidUserNameException> {
            registerUseCase
                .execute("", "password123", UserRole.MATE)
        }

        assertThat(thrownException).isEqualTo(exception)
        verify(exactly = 1) {
            repository
                .registerUser("", "password123", UserRole.MATE)
        }
    }

    @Test
    fun `execute should handle invalid password exception`() {
        // Given
        val exception = InvalidPasswordException("Password cannot be blank")
        every {
            repository
                .registerUser("username", "", UserRole.MATE)
        } throws exception

        // When/Then
        val thrownException = assertThrows<InvalidPasswordException> {
            registerUseCase
                .execute("username", "", UserRole.MATE)
        }

        assertThat(thrownException).isEqualTo(exception)
        verify(exactly = 1) {
            repository
                .registerUser("username", "", UserRole.MATE)
        }
    }
}