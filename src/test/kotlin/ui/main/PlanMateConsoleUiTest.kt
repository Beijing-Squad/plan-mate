package ui.main

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.main.consoleIO.ConsoleIO

class PlanMateConsoleUiTest {
    private lateinit var planMateUi: PlanMateUi
    private lateinit var consoleIO: ConsoleIO
    private lateinit var screens: List<BaseScreen>

    @BeforeEach
    fun setup() {
        consoleIO = mockk(relaxed = true)
        screens = listOf(
            mockk<BaseScreen>().apply {
                every { id } returns "1"
                every { name } returns "User Management"
                every { execute() } returns Unit
            },
            mockk<BaseScreen>().apply {
                every { id } returns "2"
                every { name } returns "Login"
                every { execute() } returns Unit
            }
        )
        planMateUi = PlanMateConsoleUi(
            screens,
            consoleIO
        )

    }

    @Test
    fun `should show message welcome when start app`() {
        // Given
        every { consoleIO.read() } returns EXIT_INPUT

        // When
        planMateUi.start()

        // When && Then
        verify { consoleIO.show(any()) }
    }

    @Test
    fun `should present feature when choice is one`() {
        // Given
        every { consoleIO.read() } returns INPUT_CHOICE andThen EXIT_INPUT

        // When
        planMateUi.start()

        // Then
        verify { consoleIO.showWithLine(any()) }
    }

    @Test
    fun `should view message when write invalid option`() {
        val firstChoice = "1a2b"
        val secondChoice = " -@"
        // Given
        every { consoleIO.read() } returnsMany listOf(firstChoice, secondChoice, EXIT_INPUT)

        // When
        planMateUi.start()

        // Then
        verify {
            consoleIO.showWithLine(any())
        }
    }

    @Test
    fun `should exit when choice zero from menu`() {
        // Given
        every { consoleIO.read() } returns EXIT_INPUT

        // When
        planMateUi.start()

        // Then
        verify { consoleIO.showWithLine(any()) }

    }

    @Test
    fun `should handle exception when an unknown error occurred`() {
        // Given
        every { consoleIO.read() } throws Exception("An unknown error occurred")

        // When
        planMateUi.start()

        // Then
        verify { consoleIO.showWithLine("An unknown error occurred") }
    }

    @Test
    fun `should execute feature when valid option is selected`() {
        // Given
        every { consoleIO.read() } returns INPUT_CHOICE andThen EXIT_INPUT

        // When
        planMateUi.start()

        // Then
        verify {
            screens[0].execute()
        }
    }


    private companion object {
        const val EXIT_INPUT = "0"
        const val INPUT_CHOICE = "1"
    }

}
