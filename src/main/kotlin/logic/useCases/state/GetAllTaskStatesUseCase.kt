package logic.useCases.state

import logic.entities.TaskState
import logic.entities.exceptions.StateNotFoundException
import logic.repository.StatesRepository

class GetAllTaskStatesUseCase(
    private val statesRepository: StatesRepository
) {
    fun getAllStates(): List<TaskState>{
        require(statesRepository.getAllStates().isNotEmpty()) {
            throw StateNotFoundException("No States Found")
        }
        return statesRepository.getAllStates()
    }
}