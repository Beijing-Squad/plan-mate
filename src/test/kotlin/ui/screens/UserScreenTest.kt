package ui.screens

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import logic.useCases.user.GetAllUsersUseCase
import logic.useCases.user.GetUserByUserIdUseCase
import logic.useCases.user.UpdateUserUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.main.ConsoleIO

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
        every { consoleIO.read() } returns "0"
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
        verify {consoleIO.showWithLine(any())}
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


}