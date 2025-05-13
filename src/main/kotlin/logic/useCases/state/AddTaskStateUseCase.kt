package logic.useCases.state

import logic.entity.TaskState
import logic.repository.StatesRepository

class AddTaskStateUseCase(
    private val statesRepository: StatesRepository,
) {
    suspend fun addTaskState(taskState: TaskState): Boolean {
        return statesRepository.addTaskState(taskState)
    }
}