package logic.useCases.state

import logic.entity.TaskState
import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GetTaskStateByIdUseCase(
    private val statesRepository: StatesRepository
) {
    suspend fun getTaskStateById(taskStateId: Uuid): TaskState {
        return statesRepository.getTaskStateById(taskStateId)
    }
}