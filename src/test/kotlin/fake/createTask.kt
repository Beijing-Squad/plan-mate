package fake

import kotlinx.datetime.LocalDateTime
import logic.entity.Task
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createTask(
    projectId: String = "defaultProjectId",
    title: String = "defaultTitle",
    description: String = "defaultDescription",
    createdBy: String = "defaultCreator",
    stateId: String = "defaultStateId",
    createdAt: LocalDateTime = LocalDateTime(2023, 1, 1,0,0),
    updatedAt: LocalDateTime = LocalDateTime(2023, 1, 1,0,0)
): Task {
    return Task(
        id = Uuid.random(),
        projectId = projectId,
        title = title,
        description = description,
        createdBy = createdBy,
        stateId = stateId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}