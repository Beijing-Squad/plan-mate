package fake

import kotlinx.datetime.LocalDate
import logic.entities.Project
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createProject(
    name: String = "defaultProjectName",
    description: String = "defaultDescription",
    createdBy: String = "defaultCreator",
    createdAt: LocalDate = LocalDate(2023, 1, 1),
    updatedAt: LocalDate = LocalDate(2023, 1, 1)
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