package data.csvDataSource

import com.google.common.truth.Truth.assertThat
import data.local.csvDataSource.UserCsvDataSourceImpl
import data.local.csvDataSource.csv.CsvDataSourceImpl
import data.repository.PasswordHashingDataSource
import data.repository.ValidationUserDataSource
import data.repository.dataSource.UserDataSource
import fake.createUser
import io.mockk.every
import io.mockk.mockk
import logic.entities.User
import logic.entities.exceptions.UserNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

class UserCsvDataSourceImplTest {

    private lateinit var userCsvDataSourceImpl: UserDataSource
    private lateinit var csvDataSourceImpl: CsvDataSourceImpl<User>
    private lateinit var validationUserDataSource: ValidationUserDataSource
    private lateinit var passwordHashingDataSource: PasswordHashingDataSource
    private lateinit var testUsers: List<User>


    @BeforeEach
    fun setUp() {
        csvDataSourceImpl = mockk()
        validationUserDataSource = mockk()
        passwordHashingDataSource = mockk()
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
        every { csvDataSourceImpl.loadAllDataFromFile() } returns testUsers
        userCsvDataSourceImpl = UserCsvDataSourceImpl(csvDataSourceImpl,validationUserDataSource,passwordHashingDataSource)

        // When
        val result = userCsvDataSourceImpl.getAllUsers()

        // Then
        assertThat(result).isEqualTo(testUsers)
    }

    @Test
    fun `should return empty list when data source has no users`() {
        // Given
        every { csvDataSourceImpl.loadAllDataFromFile() } returns emptyList()
        userCsvDataSourceImpl = UserCsvDataSourceImpl(csvDataSourceImpl,validationUserDataSource,passwordHashingDataSource)

        // When
        val result = userCsvDataSourceImpl.getAllUsers()

        // Then
        assertThat(result).isEmpty()
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return user when user id is founded`() {
        // Given
        every { csvDataSourceImpl.loadAllDataFromFile() } returns testUsers
        userCsvDataSourceImpl = UserCsvDataSourceImpl(csvDataSourceImpl,validationUserDataSource,passwordHashingDataSource)
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
        every { csvDataSourceImpl.loadAllDataFromFile() } returns testUsers
        userCsvDataSourceImpl = UserCsvDataSourceImpl(csvDataSourceImpl,validationUserDataSource,passwordHashingDataSource)
        val nonExistentUserId = "non-existent-id"

        // When/Then
        assertThrows<UserNotFoundException> {
            userCsvDataSourceImpl.getUserByUserId(nonExistentUserId)
        }
    }
}