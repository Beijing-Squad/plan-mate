package ui.console

import logic.entities.State
import logic.entities.Task
import ui.main.consoleIO.ConsoleIO
class SwimlanesRenderer(
    private val consoleIO: ConsoleIO
) {
    fun render(tasks: List<Task>, states: List<State>) {
        if (states.isEmpty()) {
            consoleIO.showWithLine("⚠️ No states available.")
            return
        }
        if (tasks.isEmpty()) {
            consoleIO.showWithLine("⚠️ No tasks available.")
            return
        }

        val tasksByState = states.associate { state ->
            state.id to tasks.filter { it.stateId == state.id }
        }

        val columnWidth = 20
        val maxTitleLength = columnWidth - 6

        val headerLine = states.joinToString(" | ") { state ->
            state.name.take(columnWidth).padEnd(columnWidth)
        }
        consoleIO.showWithLine(headerLine)

        val separatorLine = "-".repeat(headerLine.length)
        consoleIO.showWithLine(separatorLine)

        val maxRows = tasksByState.values.maxOfOrNull { it.size } ?: 0

        for (row in 0 until maxRows) {
            val rowLine = states.joinToString(" | ") { state ->
                val taskList = tasksByState[state.id] ?: emptyList()
                if (row < taskList.size) {
                    val task = taskList[row]
                    val displayText = "#${row + 1}: ${task.title}".take(maxTitleLength).padEnd(columnWidth)
                    displayText
                } else {
                    " ".repeat(columnWidth)
                }
            }
            consoleIO.showWithLine(rowLine)
        }
    }
}
