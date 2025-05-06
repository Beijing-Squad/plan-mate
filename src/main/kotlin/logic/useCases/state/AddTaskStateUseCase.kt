package logic.useCases.state

import logic.entities.State
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
    fun addState(state: State): Boolean {
        val isStateExist = statesRepository.getAllStates().any {
            it.id == state.id
        }
        val isProjectExist = getAllProjectsUseCase.getAllProjects().any {
            it.id.toString() == state.projectId
        }

        if (state.name.isBlank()) throw InvalidStateNameException()

        if (isStateExist) throw StateAlreadyExistException()

        if (!isProjectExist) throw ProjectNotFoundException()

        return statesRepository.addState(state)
    }
}