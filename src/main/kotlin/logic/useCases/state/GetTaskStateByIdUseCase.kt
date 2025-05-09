package logic.useCases.state

import logic.entities.TaskState
import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi

class GetTaskStateByIdUseCase(
    private val statesRepository: StatesRepository
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getStateById(stateId: String): TaskState {
        return statesRepository.getStateById(stateId)
    }
}