package data.repository.mapper

import data.remote.mongoDataSource.dto.TaskStateDto
import logic.entity.TaskState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun TaskStateDto.toTaskStateEntity(): TaskState {
    return TaskState(
        id = Uuid.parse(this.id),
        name = this.name,
        projectId = Uuid.parse(this.projectId)
    )
}

@OptIn(ExperimentalUuidApi::class)
fun TaskState.toTaskStateDto(): TaskStateDto {
    return TaskStateDto(
        id = this.id.toString(),
        name = this.name,
        projectId = this.projectId.toString()
    )
}