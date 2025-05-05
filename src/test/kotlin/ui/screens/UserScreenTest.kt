package ui.screens

import fake.createUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import logic.entities.User
import logic.entities.UserRole
import logic.entities.exceptions.InvalidPasswordException
import logic.entities.exceptions.InvalidUserNameException
import logic.useCases.authentication.SessionManagerUseCase
import logic.useCases.user.GetAllUsersUseCase
import logic.useCases.user.GetUserByIdUseCase
import logic.useCases.user.UpdateUserUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.main.consoleIO.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UserScreenTest {
    private lateinit var getAllUsersUseCase: GetAllUsersUseCase
    private lateinit var updateUsersUseCase: UpdateUserUseCase
    private lateinit var getUserByIdUseCase: GetUserByIdUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var userScreen: UserScreen
    private lateinit var sessionManager: SessionManagerUseCase

    @BeforeEach
    fun setUp() {
        getUserByIdUseCase = GetUserByIdUseCase(mockk())
        updateUsersUseCase = UpdateUserUseCase(
            mockk(relaxed = true)
        )
        sessionManager = mockk(relaxed = true)
        getAllUsersUseCase = GetAllUsersUseCase(mockk())
        consoleIO = mockk(relaxed = true)
        userScreen = UserScreen(
            getAllUsersUseCase,
            getUserByIdUseCase,
            updateUsersUseCase,
            consoleIO,
            sessionManager
        )
    }

    @Test
    fun `should show option when navigate to user screen`() {
        // Given && When
        userScreen.showOptionService()

        // Then
        verify {
            consoleIO.showWithLine(any())
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should call onClickGetAllUsers when user role is admin`() {
        // Given
        val userId = Uuid.parse(FAKE_ID)
        val mockUser = mockk<User>(relaxed = true) {
            every { id } returns userId
            every { userName } returns FAKE_USERNAME
            every { role } returns UserRole.ADMIN
        }
        every { consoleIO.read() } returns GET_ALL_USER_OPTION andThen EXIT_CHOICE
        every { sessionManager.getCurrentUser() } returns mockUser
        every { getAllUsersUseCase.getAllUsers() } returns listOf(mockUser)

        // When
        userScreen.handleFeatureChoice()

        // Then
        verify(exactly = 1) {
            getAllUsersUseCase.getAllUsers()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should not call onClickGetAllUsers when user role is mate`() {
        // Given
        val userId = Uuid.parse(FAKE_ID)
        val mockUser = mockk<User>(relaxed = true) {
            every { id } returns userId
            every { userName } returns FAKE_USERNAME
            every { role } returns UserRole.MATE
        }
        every { consoleIO.read() } returns GET_ALL_USER_OPTION andThen EXIT_CHOICE
        every { sessionManager.getCurrentUser() } returns mockUser
        every { getAllUsersUseCase.getAllUsers() } returns listOf(mockUser)

        // When
        userScreen.handleFeatureChoice()

        // Then
        verify(exactly = 0) {
            getAllUsersUseCase.getAllUsers()
        }
    }

    @Test
    fun `should call onClickGetUserByID when input is 2`() {
        // Given
        every { consoleIO.read() } returns GET_USER_BY_USER_ID andThen EXIT_CHOICE

        // When
        userScreen.handleFeatureChoice()

        // Then
        verifyOrder {
            consoleIO.showWithLine(any())
            consoleIO.show(any())
        }
    }

    @Test
    fun `should return when input is 0`() {
        // Given
        every { consoleIO.read() } returns EXIT_CHOICE
        every { getUserByIdUseCase.getUserByUserId(any()) } returns mockk(relaxed = true)

        // When
        userScreen.handleFeatureChoice()

        // Then
        verify(exactly = 0) {
            getAllUsersUseCase.getAllUsers()
        }
    }

    @Test
    fun `should show error message when inter invalid input`() {
        // Given
        every { consoleIO.read() } returnsMany listOf(
            INVALID_INPUT,
            EXIT_CHOICE
        )

        // When
        userScreen.handleFeatureChoice()

        // Then
        verify { consoleIO.showWithLine(any()) }
    }

    @Test
    fun `should show error message when input is empty`() {
        // Given
        every { consoleIO.read() } returnsMany listOf(
            EMPTY_STRING,
            EXIT_CHOICE
        )

        // When
        userScreen.handleFeatureChoice()

        // Then
        verify { consoleIO.showWithLine(any()) }
    }

    @Test
    fun `should show all users when have users in data`() {
        // Given
        every { getAllUsersUseCase.getAllUsers() } returns listOf(createUser(FAKE_USERNAME))
        every { consoleIO.read() } returnsMany listOf(
            GET_ALL_USER_OPTION,
            EXIT_CHOICE
        )
        // When
        userScreen.handleFeatureChoice()

        // Then
        verify { consoleIO.showWithLine(any()) }

    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should handle update user flow correctly when user name valid`() {
        // Given
        val userId = Uuid.parse(FAKE_ID)
        val mockUser = mockk<User>(relaxed = true) {
            every { id } returns userId
            every { userName } returns FAKE_USERNAME
        }

        every { consoleIO.read() } returnsMany listOf(
            UPDATE_USER_OPTION,
            FAKE_ID,
            UPDATE_USERNAME_OPTION,
            FAKE_USERNAME,
            EXIT_CHOICE
        )
        every { getAllUsersUseCase.getAllUsers() } returns listOf(mockUser)
        every { getUserByIdUseCase.getUserByUserId(FAKE_ID) } returns mockUser

        // When
        userScreen.handleFeatureChoice()

        // Then
        verify {
            consoleIO.showWithLine(any())
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should handle password update with confirmation when password is valid`() {
        // Given
        val userId = Uuid.parse(FAKE_ID)
        val mockUser = mockk<User>(relaxed = true) {
            every { id } returns userId
            every { userName } returns FAKE_USERNAME
            every { password } returns FAKE_PASSWORD
        }


        every { consoleIO.read() } returnsMany listOf(
            UPDATE_USER_OPTION,
            userId.toString(),
            UPDATE_PASSWORD_OPTION,
            FAKE_PASSWORD,
            FAKE_PASSWORD,
            EXIT_CHOICE
        )

        every { getUserByIdUseCase.getUserByUserId(userId.toString()) } returns mockUser
        every { sessionManager.getCurrentUser() } returns mockUser

        // When
        userScreen.handleFeatureChoice()

        // Then
        verifyOrder {
            consoleIO.show(any())
            consoleIO.show(any())
            consoleIO.showWithLine(any())
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should handle password mismatch during update when enter valid password`() {
        // Given
        val userId = Uuid.parse(FAKE_ID)
        val mockUser = mockk<User>(relaxed = true) {
            every { id } returns userId
            every { userName } returns FAKE_USERNAME
        }

        every { consoleIO.read() } returnsMany listOf(
            UPDATE_USER_OPTION,
            FAKE_ID,
            UPDATE_PASSWORD_OPTION,
            FAKE_PASSWORD,
            FAKE_PASSWORD,
            EXIT_CHOICE
        )

        every { getUserByIdUseCase.getUserByUserId(FAKE_ID) } returns mockUser

        // When
        userScreen.handleFeatureChoice()

        // Then
        verify {
            consoleIO.showWithLine(any())
        }

    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should update user name when choice update user name`() {
        // Given
        val userId = Uuid.parse(FAKE_ID)
        val mockUser = mockk<User>(relaxed = true) {
            every { id } returns userId
            every { userName } returns FAKE_USERNAME
        }

        every { sessionManager.getCurrentUser() } returns mockUser
        every { consoleIO.read() } returnsMany listOf(
            UPDATE_USER_OPTION,
            UPDATE_USERNAME_OPTION,
            FAKE_USERNAME,
            EXIT_CHOICE
        )

        every { getUserByIdUseCase.getUserByUserId(FAKE_ID) } returns mockUser

        // When
        userScreen.handleFeatureChoice()

        // Then
        verify {
            consoleIO.showWithLine(any())
        }

    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should show message error when user name is empty`() {
        // Given
        val userId = Uuid.parse(FAKE_ID)
        val mockUser = mockk<User>(relaxed = true) {
            every { id } returns userId
            every { userName } returns FAKE_USERNAME
        }

        every { sessionManager.getCurrentUser() } returns mockUser
        every { consoleIO.read() } returnsMany listOf(
            UPDATE_USER_OPTION,
            UPDATE_USERNAME_OPTION,
            EMPTY_STRING,
            EXIT_CHOICE
        )

        every { getUserByIdUseCase.getUserByUserId(FAKE_ID) } returns mockUser

        // When
        userScreen.handleFeatureChoice()

        // Then
        verify {
            consoleIO.showWithLine(any())
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should catch invalidUserNameException when user name is empty`() {
        // Given
        val userId = Uuid.parse(FAKE_ID)
        val mockUser = mockk<User>(relaxed = true) {
            every { id } returns userId
            every { userName } returns FAKE_USERNAME
        }

        every { sessionManager.getCurrentUser() } returns mockUser
        every { consoleIO.read() } returnsMany listOf(
            UPDATE_USER_OPTION,
            UPDATE_USERNAME_OPTION,
            EMPTY_STRING,
            EXIT_CHOICE
        )

        every { getUserByIdUseCase.getUserByUserId(FAKE_ID) } returns mockUser
        every {
            updateUsersUseCase.updateUser(mockUser)
        } throws InvalidUserNameException(message = "invalid user name")

        // When
        userScreen.handleFeatureChoice()

        // Then
        verify {
            consoleIO.showWithLine(any())
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should catch invalidPasswordException when password is empty`() {
        // Given
        val userId = Uuid.parse(FAKE_ID)
        val mockUser = mockk<User>(relaxed = true) {
            every { id } returns userId
            every { userName } returns FAKE_USERNAME
        }

        every { sessionManager.getCurrentUser() } returns mockUser
        every { consoleIO.read() } returnsMany listOf(
            UPDATE_USER_OPTION,
            UPDATE_PASSWORD_OPTION,
            EMPTY_STRING,
            EMPTY_STRING,
            EXIT_CHOICE
        )

        every { getUserByIdUseCase.getUserByUserId(FAKE_ID) } returns mockUser
        every {
            updateUsersUseCase.updateUser(mockUser)
        } throws InvalidPasswordException(message = "invalid password")

        // When
        userScreen.handleFeatureChoice()

        // Then
        verify {
            consoleIO.showWithLine(any())
        }
    }

    @Test
    fun `should handle user not found during update when enter invalid id`() {
        // Given
        every { consoleIO.read() } returns UPDATE_USER_OPTION andThen INVALID_FAKE_ID andThen EXIT_CHOICE
        every {
            getUserByIdUseCase.getUserByUserId(INVALID_FAKE_ID)
        } throws Exception("User not found")

        // When
        userScreen.handleFeatureChoice()

        // Then
        verify {
            consoleIO.showWithLine(any())
        }
    }

    private companion object {
        const val FAKE_ID = "117ae359-8dac-443f-a132-35b015b4a812"
        const val INVALID_FAKE_ID = "invalid-id"
        const val FAKE_USERNAME = "testUser"
        const val EXIT_CHOICE = "0"
        const val EMPTY_STRING = ""
        const val INVALID_INPUT = "999"
        const val GET_ALL_USER_OPTION = "1"
        const val UPDATE_USERNAME_OPTION = "1"
        const val UPDATE_PASSWORD_OPTION = "2"
        const val GET_USER_BY_USER_ID = "2"
        const val UPDATE_USER_OPTION = "3"
        const val FAKE_PASSWORD = "12345678"
    }

}