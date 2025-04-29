package logic.useCases.user

import com.google.common.truth.Truth.assertThat
import data.repository.UserRepositoryImpl
import fake.createUser
import io.mockk.every
import io.mockk.mockk
import logic.entities.User
import logic.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllUsersUseCaseTest {
    private lateinit var getAllUsers: GetAllUsersUseCase
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository = UserRepositoryImpl(mockk(relaxed = true))
        getAllUsers = GetAllUsersUseCase(userRepository)
    }

    @Test
    fun `should return all users when have users`() {
        // Given && When
        val users = listOf(
            createUser(userName = "mohammed1234", password = "12345678"),
            createUser(userName = "mohammed123", password = "12345678"),
            createUser(userName = "mohammed121234", password = "12345678"),
            createUser(userName = "mohammed123423424", password = "12345678"),
        )

        every {   userRepository.getAllUsers() } returns users

        // Then
        assertThat(getAllUsers.getAllUsers()).isEqualTo(users)

    }
}