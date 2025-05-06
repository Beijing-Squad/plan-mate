package fake

import kotlinx.datetime.*
import logic.entities.Project
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createProject(
    name: String = "defaultProjectName",
    description: String = "defaultDescription",
    createdBy: String = "defaultCreator",
    createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    updatedAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
): Project {
    return Project(
        id = Uuid.random(),
        name = name,
        description = description,
        createdBy = createdBy,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}