package logic.useCases.task

import logic.repository.TasksRepository

class DeleteTaskUseCase(
    private val tasksRepository: TasksRepository
) {
      suspend fun  deleteTask(taskId : String) = tasksRepository.deleteTask(taskId)
}