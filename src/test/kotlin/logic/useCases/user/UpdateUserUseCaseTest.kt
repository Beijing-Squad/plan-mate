package logic.useCases.user

import com.google.common.truth.Truth.assertThat
import data.repository.UserRepositoryImpl
import fake.createUser
import io.mockk.every
import io.mockk.mockk
import logic.entities.User
import logic.repository.UserRepository
import logic.useCases.authentication.MD5PasswordUseCase
import logic.useCases.authentication.SessionManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi

class UpdateUserUseCaseTest {
    private lateinit var updateUser: UpdateUserUseCase
    private lateinit var userRepository: UserRepository
    private lateinit var mD5Password: MD5PasswordUseCase
    private lateinit var sessionManager: SessionManager

    @BeforeEach
    fun setUp() {
        userRepository = UserRepositoryImpl(mockk(relaxed = true))
        mD5Password = mockk(relaxed = true)
        sessionManager = mockk(relaxed = true)
        updateUser = UpdateUserUseCase(userRepository, mD5Password, sessionManager)
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

        every { sessionManager.getCurrentUser() } returns mohammed
        val actual = updateUser.updateUser(userUpdated)

        // Then
        assertThat(actual.isSuccess).isTrue()
    }

    @Test
    fun `should throw InvalidUserNameException when username is invalid`() {
        // Given
        val user = createUser(userName = "")

        // When
        val actual = updateUser.updateUser(user)

        // Given
        assertThat(actual.isFailure).isTrue()

    }

    @Test
    fun `should throw InvalidPasswordException when password is invalid`() {
        // Given
        val user = createUser(password = "")

        // When
        val actual = updateUser.updateUser(user)

        // Given
        assertThat(actual.isFailure).isTrue()

    }

}