package logic.useCases.task

import logic.entities.Task
import logic.entities.exceptions.InvalidInputException
import logic.repository.TasksRepository
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class UpdateTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    fun updateTask(task: Task): Task {
        if (task.title.isEmpty()) throw InvalidInputException("Task title cannot be empty")
        return tasksRepository.updateTask(task)
    }
}