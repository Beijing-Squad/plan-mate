package fake

import kotlinx.datetime.LocalDate
import logic.entities.Task
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createTask(
    projectId: String = "defaultProjectId",
    title: String = "defaultTitle",
    description: String = "defaultDescription",
    createdBy: String = "defaultCreator",
    stateId: String = "defaultStateId",
    createdAt: LocalDate = LocalDate(2023, 1, 1),
    updatedAt: LocalDate = LocalDate(2023, 1, 1)
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