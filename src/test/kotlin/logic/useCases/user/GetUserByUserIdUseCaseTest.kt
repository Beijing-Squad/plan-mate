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

class GetUserByUserIdUseCaseTest {

    private lateinit var getUserByUserId: GetUserByIdUseCase
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository = UserRepositoryImpl(mockk())
        getUserByUserId = GetUserByIdUseCase(userRepository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return user when user id is founded`() {
        // Given
        val firstUser = createUser("mohammed123", password = "12345678")
        coEvery { userRepository.getUserByUserId(firstUser.id.toString()) } returns firstUser

        // When && Then
        runTest {
            val actual = getUserByUserId.getUserByUserId(firstUser.id.toString())
            assertThat(actual).isEqualTo(firstUser)

        }
    }
}