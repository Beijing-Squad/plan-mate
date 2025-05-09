package logic.useCases.authentication

import com.google.common.truth.Truth.assertThat
import fake.createUser
import io.mockk.every
import io.mockk.mockk
import logic.entities.User
import logic.entities.UserRole
import logic.entities.exceptions.UserExistsException
import logic.repository.AuthenticationRepository
import logic.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RegisterUserAuthenticationUseCaseTest() {
    private lateinit var repository: AuthenticationRepository
    private lateinit var userRepository: UserRepository
    private lateinit var useCase: RegisterUserAuthenticationUseCase
    private lateinit var mD5PasswordUseCase: MD5PasswordUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        mD5PasswordUseCase = mockk(relaxed = true)
        useCase = RegisterUserAuthenticationUseCase(repository, userRepository,mD5PasswordUseCase)
    }

    @Test
    fun `should register new user successfully`() {
        // Given
        val user = createTestUser()
        every { userRepository.getAllUsers() } returns emptyList()
        every { repository.registerUser(any()) } returns true

        /// When
        val result = useCase.execute(user.userName, user.password, UserRole.MATE)

        //Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should throw exception when user already exists`() {
        // Given
        val user = createTestUser()
        every { userRepository.getAllUsers() } returns listOf(user)

        // When & Then
        assertThrows<UserExistsException> {
            useCase.execute(user.userName, user.password, UserRole.MATE)
        }
    }

    private fun createTestUser(): User {
        return createUser(userName = "mohamed", password = "pass123", role = UserRole.MATE)
    }
}