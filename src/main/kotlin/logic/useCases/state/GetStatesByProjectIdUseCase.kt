package logic.useCases.state

import logic.entities.State
import logic.entities.exceptions.StateNotFoundException
import logic.repository.StatesRepository

class GetStatesByProjectIdUseCase(
    private val statesRepository: StatesRepository
) {
    fun getStatesByProjectId(projectId: String): List<State> {
        return statesRepository.getStatesByProjectId(projectId)
            .also {
                if (it.isEmpty()) {
                    throw StateNotFoundException("the state with this $projectId project id not found")
                }
            }
    }
}