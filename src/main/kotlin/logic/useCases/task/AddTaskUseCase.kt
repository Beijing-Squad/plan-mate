package logic.useCases.task

import logic.entities.Task
import logic.repository.TasksRepository

class AddTaskUseCase(
    private val tasksRepository: TasksRepository
) {
     fun addTask(task: Task) = tasksRepository.addTask(task)
}