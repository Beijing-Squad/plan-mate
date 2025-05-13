package logic.useCases.task

import logic.entity.Task
import logic.repository.TasksRepository

class GetAllTasksUseCase(
    private val tasksRepository: TasksRepository
) {
      suspend fun getAllTasks(): List<Task> {
          return tasksRepository.getAllTasks()
    }
}