package logic.useCases.state

import logic.entities.TaskState
import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class UpdateTaskStateUseCase(
    private val statesRepository: StatesRepository
) {
    suspend fun updateTaskState(taskState: TaskState): Boolean {
        return statesRepository.updateTaskState(taskState)
    }
}