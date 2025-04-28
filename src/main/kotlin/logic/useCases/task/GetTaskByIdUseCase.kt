package logic.useCases.task

import logic.repository.TasksRepository

class GetTaskByIdUseCase(
    private val tasksRepository: TasksRepository
) {
}
