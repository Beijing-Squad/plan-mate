package data.csvDataSource

import com.google.common.truth.Truth.assertThat
import data.local.csvDataSource.AuthenticationCsvDataSourceImpl
import data.local.csvDataSource.csv.CsvDataSourceImpl
import data.repository.PasswordHashingDataSource
import data.repository.ValidationUserDataSource
import data.repository.dataSource.UserDataSource
import fake.createUser
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.User
import logic.entities.UserRole
import logic.entities.exceptions.InvalidPasswordException
import logic.entities.exceptions.InvalidUserNameException
import logic.entities.exceptions.UserExistsException
import logic.entities.exceptions.UserNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AuthenticationCsvDataSourceImplTest {

    private val csvDataSource = mockk<CsvDataSourceImpl<User>>(relaxed = true)
    private val userDataSource = mockk<UserDataSource>()
    private lateinit var validationUserDataSource: ValidationUserDataSource
    private lateinit var passwordHashingDataSource: PasswordHashingDataSource
    private lateinit var authDataSource: AuthenticationCsvDataSourceImpl

    private val testUser = createUser(
        userName = "mohamed",
        password = "5f4dcc3b5aa765d61d8327deb882cf99",
        role = UserRole.ADMIN
    )

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        validationUserDataSource = mockk(relaxed = true)
        passwordHashingDataSource = mockk(relaxed = true)
        authDataSource = AuthenticationCsvDataSourceImpl(csvDataSource, userDataSource,validationUserDataSource,passwordHashingDataSource)
    }

    @Test
    fun `getAuthenticatedUser should throw UserNotFoundException when user does not exist`() {
        // Given
        every { userDataSource.getAllUsers() } returns emptyList()

        // When/Then
        assertThrows<UserNotFoundException> {
            authDataSource.getAuthenticatedUser("nonexistent", "password123")
        }
        verify(exactly = 1) { userDataSource.getAllUsers() }
    }

    @Test
    fun `getAuthenticatedUser should throw InvalidPasswordException when password is incorrect`() {
        // Given
        val users = listOf(testUser)
        every { userDataSource.getAllUsers() } returns users

        // When/Then
        assertThrows<InvalidPasswordException> {
            authDataSource.getAuthenticatedUser("mohamed", "wrongpassword")
        }
        verify(exactly = 1) { userDataSource.getAllUsers() }
    }

    @Test
    fun `getAuthenticatedUser should throw InvalidUserNameException when username is blank`() {
        // Given
        every { validationUserDataSource.validateUsername("") } throws InvalidUserNameException("Invalid username")

        // When/Then
        assertThrows<InvalidUserNameException> {
            authDataSource.getAuthenticatedUser("", "password123")
        }
        verify(exactly = 1) { validationUserDataSource.validateUsername("") }
        verify(exactly = 0) { userDataSource.getAllUsers() }
    }


    @Test
    fun `getAuthenticatedUser should throw InvalidPasswordException when password is blank`() {
        // Given
        every { validationUserDataSource.validatePassword("") } throws InvalidPasswordException("Invalid password")

        // When/Then
        assertThrows<InvalidPasswordException> {
            authDataSource.getAuthenticatedUser("mohamed", "")
        }
        verify(exactly = 1) { validationUserDataSource.validatePassword("") }
        verify(exactly = 0) { userDataSource.getAllUsers() }
    }

    @Test
    fun `saveUser should add new user and return true when successful`() {
        // Given
        every { userDataSource.getAllUsers() } returns emptyList()
        every { csvDataSource.appendToFile(any()) } returns Unit

        // When
        val result = authDataSource.saveUser("newuser", "password123", UserRole.MATE)

        // Then
        assertThat(result).isTrue()
        verify(exactly = 1) { userDataSource.getAllUsers() }
        verify(exactly = 1) { csvDataSource.appendToFile(any()) }
    }

    @Test
    fun `saveUser should throw UserExistsException when username already exists`() {
        // Given
        every { userDataSource.getAllUsers() } returns listOf(testUser)

        // When/Then
        assertThrows<UserExistsException> {
            authDataSource.saveUser("mohamed", "newpassword", UserRole.MATE)
        }
        verify(exactly = 1) { userDataSource.getAllUsers() }
        verify(exactly = 0) { csvDataSource.appendToFile(any()) }
    }


    @Test
    fun `saveUser should return false when an exception occurs during saving`() {
        // Given
        every { userDataSource.getAllUsers() } returns emptyList()
        every { csvDataSource.appendToFile(any()) } throws RuntimeException("CSV write error")

        // When
        val result = authDataSource.saveUser("newuser", "password123", UserRole.MATE)

        // Then
        assertThat(result).isFalse()
        verify(exactly = 1) { userDataSource.getAllUsers() }
        verify(exactly = 1) { csvDataSource.appendToFile(any()) }
    }

}