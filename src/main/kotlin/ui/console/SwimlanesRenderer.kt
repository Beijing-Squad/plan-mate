package ui.console

import logic.entities.State
import logic.entities.Task
import ui.main.consoleIO.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi

class SwimlanesRenderer(
    private val consoleIO: ConsoleIO
) {
    fun render(tasks: List<Task>, states: List<State>) {
        val lines = renderToLines(tasks, states)
        lines.forEach { consoleIO.showWithLine(it) }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun renderToLines(tasks: List<Task>, states: List<State>): List<String> {
        if (states.isEmpty()) {
            return listOf("⚠️ No states available.")
        }
        if (tasks.isEmpty()) {
            return listOf("⚠️ No tasks available.")
        }

        val lines = mutableListOf<String>()
        val tasksByState = states.associate { state ->
            state.id to tasks.filter { it.stateId == state.id.toString() }
        }

        val columnWidth = 20
        val maxTitleLength = columnWidth - 6

        val headerLine = states.joinToString(" | ") { state ->
            state.name.take(columnWidth).padEnd(columnWidth)
        }
        lines.add(headerLine)

        val separatorLine = "-".repeat(headerLine.length)
        lines.add(separatorLine)

        val maxRows = tasksByState.values.maxOfOrNull { it.size } ?: 0

        for (row in 0 until maxRows) {
            val rowLine = states.joinToString(" | ") { state ->
                val taskList = tasksByState[state.id] ?: emptyList()
                if (row < taskList.size) {
                    val task = taskList[row]
                    "#${row + 1}: ${task.title}".take(maxTitleLength).padEnd(columnWidth)
                } else {
                    " ".repeat(columnWidth)
                }
            }
            lines.add(rowLine)
        }

        return lines
    }
}
