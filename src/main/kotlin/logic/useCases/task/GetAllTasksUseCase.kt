package logic.useCases.task

import logic.repository.TasksRepository

class GetAllTasksUseCase(
    private val tasksRepository: TasksRepository
) {
}