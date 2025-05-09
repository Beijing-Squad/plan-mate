package data.repository.mapper

import data.dto.TaskDTO
import kotlinx.datetime.LocalDateTime
import logic.entities.Task
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun toTaskEntity(taskDTO: TaskDTO): Task {
    return Task(
        id = Uuid.parse(taskDTO.id),
        projectId = taskDTO.projectId,
        title = taskDTO.title,
        description = taskDTO.description,
        createdBy = taskDTO.createdBy,
        stateId = taskDTO.stateId,
        createdAt = LocalDateTime.parse(taskDTO.createdAt),
        updatedAt = LocalDateTime.parse(taskDTO.updatedAt)
    )
}
@OptIn(ExperimentalUuidApi::class)
fun toTaskDTO(task: Task): TaskDTO {
    return TaskDTO(
        id = task.id.toString(),
        projectId = task.projectId,
        title = task.title,
        description = task.description,
        createdBy = task.createdBy,
        stateId = task.stateId,
        createdAt = task.createdAt.toString(),
        updatedAt = task.updatedAt.toString()
    )
}
