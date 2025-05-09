package logic.useCases.user

import com.google.common.truth.Truth.assertThat
import data.repository.UserRepositoryImpl
import fake.createUser
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi

class GetAllUsersUseCaseTest {
    private lateinit var getAllUsers: GetAllUsersUseCase
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository = mockk(relaxed = true)
        getAllUsers = GetAllUsersUseCase(userRepository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return all users when have users`() {
        // Given && When
        val users = listOf(
            createUser(userName = "mohammed1234", password = "12345678"),
            createUser(userName = "mohammed123", password = "12345678"),
            createUser(userName = "mohammed121234", password = "12345678"),
            createUser(userName = "mohammed123423424", password = "12345678"),
        )

        coEvery { userRepository.getAllUsers() } returns users

        // Then
        runTest {
            val result = getAllUsers.getAllUsers()
            assertThat(result).isEqualTo(users)
        }

    }
}