package logic.useCases.task

import logic.repository.TasksRepository

class DeleteTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    fun  deleteTask(taskId : String) = tasksRepository.deleteTask(taskId)
}