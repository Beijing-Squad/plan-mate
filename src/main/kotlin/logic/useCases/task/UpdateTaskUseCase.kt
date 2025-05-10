package logic.useCases.task

import data.repository.mapper.toTaskEntity
import logic.entities.Task
import logic.repository.TasksRepository

class UpdateTaskUseCase(
    private val tasksRepository: TasksRepository
) {
      suspend fun updateTask(task: Task): Task {
          return tasksRepository.updateTask(task)
    }
}