package logic.useCases.state

import logic.entities.TaskState
import logic.repository.StatesRepository

class GetAllTaskStatesUseCase(
    private val statesRepository: StatesRepository
) {
    suspend fun getAllTaskStates(): List<TaskState> {
        return statesRepository.getAllTaskStates()
    }
}