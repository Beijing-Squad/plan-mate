package logic.useCases.task

import data.repository.mapper.toTaskEntity
import logic.entities.Task
import logic.repository.TasksRepository

class GetAllTasksUseCase(
    private val tasksRepository: TasksRepository
) {
      suspend fun getAllTasks(): List<Task> {
          toTaskEntity(return tasksRepository.getAllTasks())
    }
}