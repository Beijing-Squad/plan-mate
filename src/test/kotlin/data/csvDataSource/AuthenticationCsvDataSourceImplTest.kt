package data.csvDataSource

import com.google.common.truth.Truth.assertThat
import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.UserDataSource
import fake.createUser
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.User
import logic.entities.UserRole
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthenticationCsvDataSourceImplTest {

    private val csvDataSource = mockk<CsvDataSourceImpl<User>>(relaxed = true)
    private val userDataSource = mockk<UserDataSource>()
    private lateinit var authDataSource: AuthenticationCsvDataSourceImpl

    // Test data
    private val testUser = createUser(
        userName = "mohamed",
        password = "password123",
        role = UserRole.ADMIN
    )

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        authDataSource = AuthenticationCsvDataSourceImpl(csvDataSource, userDataSource)
    }

    @Test
    fun `getUserByUsername should return user when user exists`() {
        // Given
        val users = listOf(
            testUser,
            createUser(
                userName = "anotheruser",
                password = "password456",
                role = UserRole.ADMIN
            )
        )
        every { userDataSource.getAllUsers() } returns users

        // When
        val result = authDataSource.getUserByUsername("mohamed")

        // Then
        verify(exactly = 1) { userDataSource.getAllUsers() }
        assertThat(result).isEqualTo(testUser)
    }
}