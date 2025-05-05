package logic.useCases.state

import logic.entities.State
import logic.entities.exceptions.StateNotFoundException
import logic.repository.StatesRepository

class GetStatesByProjectIdUseCase(
    private val statesRepository: StatesRepository
) {
    fun getStatesByProjectId(projectId: String): List<State> {
        require(statesRepository.getStatesByProjectId(projectId).isNotEmpty()) {
            throw StateNotFoundException("No States Found")
        }
        return statesRepository.getStatesByProjectId(projectId)
    }
}