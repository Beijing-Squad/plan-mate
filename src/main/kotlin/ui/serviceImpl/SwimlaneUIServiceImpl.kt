package ui.serviceImpl

import logic.useCases.swimlanes.GetSwimlaneTasksUseCase
import ui.service.ConsoleIOService
import ui.service.SwimlaneUIService


class SwimlaneUIServiceImpl(
    private val console: ConsoleIOService,
    private val getSwimlaneTasksUseCase: GetSwimlaneTasksUseCase
) : SwimlaneUIService {

    override fun displaySwimlanes(projectId: String) {
        try {
            val result = getSwimlaneTasksUseCase.invoke(projectId)
            console.printDivider()
            console.printMessage("Project: ${result.project.name}")
            console.printDivider()

            result.statesWithTasks.forEach { (state, tasks) ->
                console.printMessage("[${state.name}]")
                if (tasks.isEmpty()) {
                    console.printMessage("- No tasks.")
                } else {
                    tasks.forEachIndexed { index, task ->
                        console.printMessage("- #${index + 1}: ${task.title} (By: ${task.createdBy})")
                    }
                }
                console.printMessage("")
            }

            console.printDivider()
        } catch (e: Exception) {
            console.printError("Failed to display swimlanes: ${e.message}")
        }
    }
}
