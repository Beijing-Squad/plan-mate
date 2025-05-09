package data.repository.mapper

import data.dto.TaskStateDTO
import logic.entities.TaskState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun toTaskStateEntity(taskState: TaskStateDTO): TaskState {
    return TaskState(
        id = Uuid.parse(taskState.id),
        name = taskState.name,
        projectId = Uuid.parse(taskState.projectId)
    )
}

@OptIn(ExperimentalUuidApi::class)
fun toTaskStateDto(taskState: TaskState): TaskStateDTO {
    return TaskStateDTO(
        id = taskState.id.toString(),
        name = taskState.name,
        projectId = taskState.projectId.toString()
    )
}