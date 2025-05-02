package logic.useCases.task

import logic.entities.Task
import logic.entities.exceptions.TaskNotFoundException
import logic.repository.TasksRepository
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class GetTaskByIdUseCase(
    private val tasksRepository: TasksRepository
) {
    fun getTaskById(taskId: String): Task {
        return tasksRepository.getTaskById(taskId)
    }
}