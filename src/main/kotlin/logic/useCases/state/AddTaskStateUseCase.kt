package logic.useCases.state

import logic.entities.TaskState
import logic.entities.exceptions.InvalidStateNameException
import logic.entities.exceptions.ProjectNotFoundException
import logic.entities.exceptions.StateAlreadyExistException
import logic.repository.StatesRepository
import logic.useCases.project.GetAllProjectsUseCase
import kotlin.uuid.ExperimentalUuidApi

class AddTaskStateUseCase(
    private val statesRepository: StatesRepository,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
) {
    @OptIn(ExperimentalUuidApi::class)
    fun addState(taskState: TaskState): Boolean {
        val isStateExist = statesRepository.getAllStates().any {
            it.id == taskState.id
        }
        val isProjectExist = getAllProjectsUseCase.getAllProjects().any {
            it.id.toString() == taskState.projectId
        }

        if (taskState.name.isBlank()) throw InvalidStateNameException()

        if (isStateExist) throw StateAlreadyExistException()

        if (!isProjectExist) throw ProjectNotFoundException()

        return statesRepository.addState(taskState)
    }
}