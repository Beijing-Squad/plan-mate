package logic.useCases.task

import logic.entities.Task
import logic.repository.TasksRepository
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class GetAllTasksUseCase(
    private val tasksRepository: TasksRepository
) {
    fun getAllTasks(): List<Task> {
        return tasksRepository.getAllTasks()
    }
}