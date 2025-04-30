package logic.useCases.task

import logic.entities.Task
import logic.entities.exceptions.InvalidInputException
import logic.entities.exceptions.TaskNotFoundException
import logic.repository.TasksRepository
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class UpdateTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    fun updateTask(taskId: String, title: String?, description: String?, currentDate: LocalDate): Task {
        if (title != null && title.isEmpty()) throw InvalidInputException("Task title cannot be empty")
        if (description != null && description.isEmpty()) throw InvalidInputException("Task description cannot be empty")

        val task = tasksRepository.getTaskById(taskId) ?: throw TaskNotFoundException("Task with ID $taskId not found")

        val updatedTask = task.copy(
            title = title ?: task.title,
            description = description ?: task.description,
            updatedAt = currentDate
        )

        return tasksRepository.updateTask(taskId, updatedTask)
    }
}