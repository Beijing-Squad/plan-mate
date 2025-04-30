package logic.useCases.user

import com.google.common.truth.Truth.assertThat
import data.repository.UserRepositoryImpl
import fake.createUser
import io.mockk.every
import io.mockk.mockk
import logic.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi

class GetUserByUserIdUseCaseTest {

    private lateinit var getUserByUserId: GetUserByUserIdUseCase
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository = UserRepositoryImpl(mockk())
        getUserByUserId = GetUserByUserIdUseCase(userRepository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return user when user id is founded`() {
        // Given
        val firstUser = createUser("mohammed123", password = "12345678")
        every { userRepository.getUserByUserId(firstUser.id.toString()) } returns firstUser

        // When
        val actual = getUserByUserId.getUserByUserId(firstUser.id.toString())

        // Then
        assertThat(actual).isEqualTo(firstUser)

    }

}