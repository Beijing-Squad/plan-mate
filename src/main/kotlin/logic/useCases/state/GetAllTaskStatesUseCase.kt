package logic.useCases.state

import logic.entities.TaskState
import logic.exceptions.StateNotFoundException
import logic.repository.StatesRepository

class GetAllTaskStatesUseCase(
    private val statesRepository: StatesRepository
) {
    suspend fun getAllStates(): List<TaskState> {
        require(statesRepository.getAllStates().isNotEmpty()) {
            throw StateNotFoundException("No States Found")
        }
        return statesRepository.getAllStates()
    }
}