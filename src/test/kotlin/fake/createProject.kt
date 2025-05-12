package fake

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import logic.entities.Project
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createProject(
    id: Uuid = Uuid.random(),
    name: String = "defaultProjectName",
    description: String = "defaultDescription",
    createdBy: String = "defaultCreator",
    createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    updatedAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
): Project {
    return Project(
        id = id,
        name = name,
        description = description,
        createdBy = createdBy,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}