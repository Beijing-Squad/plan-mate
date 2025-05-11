package logic.useCases.state

import logic.entities.TaskState
import logic.exceptions.InvalidStateNameException
import logic.exceptions.ProjectNotFoundException
import logic.exceptions.StateAlreadyExistException
import logic.repository.StatesRepository
import logic.useCases.project.GetAllProjectsUseCase
import kotlin.uuid.ExperimentalUuidApi

class AddTaskStateUseCase(
    private val statesRepository: StatesRepository,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun addState(taskState: TaskState): Boolean {
        val isStateExist = statesRepository.getAllStates().any {
            it.id == taskState.id
        }
        val isProjectExist = getAllProjectsUseCase.getAllProjects().any {
            it.id == taskState.projectId
        }

        if (taskState.name.isBlank()) throw InvalidStateNameException()

        if (isStateExist) throw StateAlreadyExistException()

        if (!isProjectExist) throw ProjectNotFoundException()

        return statesRepository.addState(taskState)
    }
}