package logic.useCases.state

import logic.entities.TaskState
import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi

class UpdateTaskStateUseCase(
    private val statesRepository: StatesRepository,
    private val getTaskStateByIdUseCase: GetTaskStateByIdUseCase
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun updateState(taskState: TaskState): TaskState {
        getTaskStateByIdUseCase.getStateById(taskState.id.toString())

        return statesRepository.updateState(taskState)
    }
}