package logic.useCases.task

import logic.entities.Task
import logic.repository.TasksRepository

class UpdateTaskUseCase(
    private val tasksRepository: TasksRepository
) {
     fun updateTask(task: Task): Task {
        return tasksRepository.updateTask(task)
    }
}