package data.repository.mapper

import data.remote.mongoDataSource.dto.TaskStateDto
import logic.entity.TaskState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun toTaskStateEntity(taskState: TaskStateDto): TaskState {
    return TaskState(
        id = Uuid.parse(taskState.id),
        name = taskState.name,
        projectId = Uuid.parse(taskState.projectId)
    )
}

@OptIn(ExperimentalUuidApi::class)
fun toTaskStateDto(taskState: TaskState): TaskStateDto {
    return TaskStateDto(
        id = taskState.id.toString(),
        name = taskState.name,
        projectId = taskState.projectId.toString()
    )
}