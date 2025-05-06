package logic.useCases.task

import logic.entities.Task
import logic.repository.TasksRepository

class GetAllTasksUseCase(
    private val tasksRepository: TasksRepository
) {
     fun getAllTasks(): List<Task> {
        return tasksRepository.getAllTasks()
    }
}