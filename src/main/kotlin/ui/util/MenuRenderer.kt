package ui.util

import ui.main.consoleIO.ConsoleIO
import kotlin.enums.EnumEntries

object MenuRenderer {
    fun <T> renderMenu(
        header: String,
        options: EnumEntries<T>,
        consoleIO: ConsoleIO
    ) where T : Enum<T>, T : MenuOption {
        consoleIO.showWithLine(header.trimIndent())
        consoleIO.showWithLine("┌─── Available Options ───────────────────┐")
        consoleIO.showWithLine("│                                         │")
        options.forEach { option ->
            val padded = option.code.padEnd(2)
            consoleIO.showWithLine("│  $padded. ${option.description.padEnd(35)}│")
            }
        consoleIO.showWithLine("│                                         │")
        consoleIO.showWithLine("└─────────────────────────────────────────┘")
        consoleIO.show("\uD83D\uDCA1 Please enter your choice: ")
    }
}

