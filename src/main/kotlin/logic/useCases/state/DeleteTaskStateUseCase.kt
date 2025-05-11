package logic.useCases.state

import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class DeleteTaskStateUseCase(
    private val statesRepository: StatesRepository,
) {
    suspend fun deleteTaskState(taskStateId: Uuid): Boolean {
        return statesRepository.deleteTaskState(taskStateId)
    }
}