package ui.screens

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import logic.entities.User
import logic.useCases.user.GetAllUsersUseCase
import logic.useCases.user.GetUserByUserIdUseCase
import logic.useCases.user.UpdateUserUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.main.consoleIO.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UserScreenTest {
    private lateinit var getAllUsersUseCase: GetAllUsersUseCase
    private lateinit var updateUsersUseCase: UpdateUserUseCase
    private lateinit var getUserByUserIdUseCase: GetUserByUserIdUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var userScreen: UserScreen

    @BeforeEach
    fun setUp() {
        getUserByUserIdUseCase = GetUserByUserIdUseCase(mockk())
        updateUsersUseCase = UpdateUserUseCase(
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
        getAllUsersUseCase = GetAllUsersUseCase(mockk())
        consoleIO = mockk(relaxed = true)
        userScreen = UserScreen(
            getAllUsersUseCase,
            getUserByUserIdUseCase,
            updateUsersUseCase,
            consoleIO

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

    @Test
    fun `should call getAllUsers when input is 1`() {
        // Given
        every { consoleIO.read() } returns "1"
        every { getAllUsersUseCase.getAllUsers() } returns emptyList()

        // When
        userScreen.handleFeatureChoice()

        // Then
        verify(exactly = 1) {
            getAllUsersUseCase.getAllUsers()
        }
    }

    @Test
    fun `should call getUserByID when input is 2`() {
        // Given
        every { consoleIO.read() } returns "2"

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
        every { getUserByUserIdUseCase.getUserByUserId(any()) } returns mockk(relaxed = true)

        // When
        userScreen.handleFeatureChoice()

        // Then
        verify(exactly = 0) {
            getAllUsersUseCase.getAllUsers()
        }
    }


    @Test
    fun `should show error message for invalid input`() {
        // Given
        every { consoleIO.read() } returns "999"

        // When
        userScreen.handleFeatureChoice()

        // Then
        verify { consoleIO.showWithLine(any()) }
    }

    @Test
    fun `should handle empty input`() {
        // Given
        every { consoleIO.read() } returns ""

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
            "1",
            "newUsername",
            EXIT_CHOICE
        )

        every { getUserByUserIdUseCase.getUserByUserId(FAKE_ID) } returns mockUser

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

        every { getUserByUserIdUseCase.getUserByUserId(userId.toString()) } returns mockUser

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

        every { getUserByUserIdUseCase.getUserByUserId(FAKE_ID) } returns mockUser

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
        every { consoleIO.read() } returns UPDATE_USER_OPTION andThen "invalid-id"
        every { getUserByUserIdUseCase.getUserByUserId("invalid-id") } throws Exception("User not found")

        // When
        userScreen.handleFeatureChoice()

        // Then
        verify {
            consoleIO.showWithLine(any())
        }
    }

    private companion object {
        const val FAKE_ID = "117ae359-8dac-443f-a132-35b015b4a812"
        const val FAKE_USERNAME = "testUser"
        const val UPDATE_USER_OPTION = "3"
        const val UPDATE_PASSWORD_OPTION = "2"
        const val EXIT_CHOICE = "0"
        const val FAKE_PASSWORD = "12345678"
    }

}