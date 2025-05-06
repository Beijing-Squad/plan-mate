package logic.useCases.state

import logic.entities.State
import logic.entities.exceptions.StateNotFoundException
import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi

class GetStatesByProjectIdUseCase(
    private val statesRepository: StatesRepository
) {
    @OptIn(ExperimentalUuidApi::class)
    fun getStatesByProjectId(projectId: String): List<State> {
        require(statesRepository.getStatesByProjectId(projectId).isNotEmpty()) {
            throw StateNotFoundException("No States Found")
        }
        return statesRepository.getStatesByProjectId(projectId)
    }
}