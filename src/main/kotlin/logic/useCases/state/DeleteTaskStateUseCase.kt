package logic.useCases.state

import logic.entities.TaskState
import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi

class DeleteTaskStateUseCase(
    private val statesRepository: StatesRepository,
    private val getTaskStateByIdUseCase: GetTaskStateByIdUseCase
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun deleteState(taskState: TaskState): Boolean {
        val existedState = getTaskStateByIdUseCase.getStateById(taskState.id.toString())
        return statesRepository.deleteState(existedState)
    }
}