package ui.console

import logic.entities.State
import logic.entities.Task
import ui.main.consoleIO.ConsoleIO

class SwimlanesRenderer(
    private val consoleIO: ConsoleIO
) {

    fun render(tasks: List<Task>, states: List<State>) {
        if (tasks.isEmpty()) {
            consoleIO.showWithLine("⚠️ No tasks to display.")
            return
        }

        val groupedTasks = tasks.groupBy { it.stateId }

        consoleIO.showWithLine("📊 Task Board (Swimlanes):")

        for (state in states) {
            consoleIO.showWithLine("\n\u001B[34m🟦 ${state.name.uppercase()} \u001B[0m")
            val tasksInState = groupedTasks[state.id] ?: emptyList()
            if (tasksInState.isEmpty()) {
                consoleIO.showWithLine("   No tasks in this state.")
            } else {
                tasksInState.forEach { task ->
                    consoleIO.showWithLine("   🔹 [${task.title}] - ${task.description}")
                }
            }
        }
    }
}
