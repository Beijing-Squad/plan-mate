package data.csvDataSource

import com.google.common.truth.Truth.assertThat
import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.UserDataSource
import fake.createUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.User
import logic.entities.exceptions.UserNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

class UserCsvDataSourceImplTest {

    private lateinit var userCsvDataSourceImpl: UserDataSource
    private lateinit var csvDataSourceImpl: CsvDataSourceImpl<User>
    private lateinit var testUsers: List<User>


    @BeforeEach
    fun setUp() {
        csvDataSourceImpl = mockk()
        testUsers = listOf(
            createUser(
                userName = "mohammed1234",
                password = "12345678"
            ),
            createUser(
                userName = "mohammed123",
                password = "12345678"
            ),
            createUser(
                userName = "mohammed121234",
                password = "12345678"
            ),
            createUser(
                userName = "mohammed123423424",
                password = "12345678"
            )
        )
    }


    @Test
    fun `should return all users when data source has users`() {
        // Given
        every { csvDataSourceImpl.loadAll() } returns testUsers
        userCsvDataSourceImpl = UserCsvDataSourceImpl(csvDataSourceImpl)

        // When
        val result = userCsvDataSourceImpl.getAllUsers()

        // Then
        assertThat(result).isEqualTo(testUsers)
    }

    @Test
    fun `should return empty list when data source has no users`() {
        // Given
        every { csvDataSourceImpl.loadAll() } returns emptyList()
        userCsvDataSourceImpl = UserCsvDataSourceImpl(csvDataSourceImpl)

        // When
        val result = userCsvDataSourceImpl.getAllUsers()

        // Then
        assertThat(result).isEmpty()
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return user when user id is founded`() {
        // Given
        every { csvDataSourceImpl.loadAll() } returns testUsers
        userCsvDataSourceImpl = UserCsvDataSourceImpl(csvDataSourceImpl)
        val firstUser = testUsers.first()

        // When
        val actual = userCsvDataSourceImpl.getUserByUserId(firstUser.id.toString())

        // Then
        assertThat(actual).isEqualTo(firstUser)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw UserNotFoundException when user id is not found`() {
        // Given
        every { csvDataSourceImpl.loadAll() } returns testUsers
        userCsvDataSourceImpl = UserCsvDataSourceImpl(csvDataSourceImpl)
        val nonExistentUserId = "non-existent-id"

        // When/Then
        assertThrows<UserNotFoundException> {
            userCsvDataSourceImpl.getUserByUserId(nonExistentUserId)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should update user when user exists`() {
        // Given
        every { csvDataSourceImpl.loadAll() } returns testUsers
        every { csvDataSourceImpl.update(any()) } returns Unit
        userCsvDataSourceImpl = UserCsvDataSourceImpl(csvDataSourceImpl)

        val existingUser = testUsers.first()
        val updatedUser = existingUser.copy(
            userName = "newUsername",
            password = "newPassword"
        )

        // When
        val result = userCsvDataSourceImpl.updateUser(updatedUser)

        // Then
        assertThat(result).isEqualTo(existingUser)
        verify { csvDataSourceImpl.update(any()) }
    }
}