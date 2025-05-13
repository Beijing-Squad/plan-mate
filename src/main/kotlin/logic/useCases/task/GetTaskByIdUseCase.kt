package logic.useCases.task

import logic.entity.Task
import logic.repository.TasksRepository

class GetTaskByIdUseCase(
    private val tasksRepository: TasksRepository
) {
      suspend fun getTaskById(taskId: String): Task {
          return tasksRepository.getTaskById(taskId)
    }
}