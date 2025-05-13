package logic.useCases.task

import logic.entity.Task
import logic.repository.TasksRepository

class UpdateTaskUseCase(
    private val tasksRepository: TasksRepository
) {
      suspend fun updateTask(task: Task): Task {
          return tasksRepository.updateTask(task)
    }
}