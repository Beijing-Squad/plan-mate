package logic.useCases.state

import logic.entities.State
import logic.repository.StatesRepository

class GetStatesByProjectIdUseCase(
    private val statesRepository: StatesRepository
) {
    fun getStatesByProjectId(projectId: String): List<State> {
        return statesRepository.getStatesByProjectId(projectId)
    }
}