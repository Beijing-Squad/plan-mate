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
        return tasksRepository.updateTask(task)
    }
}