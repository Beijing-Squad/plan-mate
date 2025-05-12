package data.repository.mapper

import data.dto.TaskDto
import kotlinx.datetime.LocalDateTime
import logic.entities.Task
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun TaskDto.toTaskEntity(): Task {
    return Task(
        id = Uuid.parse(this.id),
        projectId = this.projectId,
        title = this.title,
        description = this.description,
        createdBy = this.createdBy,
        stateId = this.stateId,
        createdAt = LocalDateTime.parse(this.createdAt),
        updatedAt = LocalDateTime.parse(this.updatedAt)
    )
}

@OptIn(ExperimentalUuidApi::class)
fun Task.toTaskDto(): TaskDto {
    return TaskDto(
        id = this.id.toString(),
        projectId = this.projectId,
        title = this.title,
        description = this.description,
        createdBy = this.createdBy,
        stateId = this.stateId,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )
}