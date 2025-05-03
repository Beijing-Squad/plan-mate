package logic.useCases.state

import logic.entities.State
import logic.entities.UserRole
import logic.entities.exceptions.ProjectNotFoundException
import logic.entities.exceptions.StateAlreadyExistException
import logic.entities.exceptions.StateUnauthorizedUserException
import logic.repository.ProjectsRepository
import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi

class AddStateUseCase(
    private val statesRepository: StatesRepository,
    private val ProjectRepository: ProjectsRepository
) {
    @OptIn(ExperimentalUuidApi::class)
    fun addState(state: State, role: UserRole): Boolean {
        if (role != UserRole.ADMIN){
            throw StateUnauthorizedUserException("user should be Admin")
        }
        if (ProjectRepository.getAllProjects().any { it.id.toString() != state.projectId }){
            throw ProjectNotFoundException("the project id with this state not found")
        }
        val allStats = statesRepository.getAllStates()
        if (allStats.any { it.id == state.id }) {
            throw StateAlreadyExistException("State with id ${state.id} already exists")
        }


        return statesRepository.addState(state)
    }
}