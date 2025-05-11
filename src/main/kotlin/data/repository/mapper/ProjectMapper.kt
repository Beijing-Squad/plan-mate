package data.repository.mapper

import data.dto.ProjectDto
import kotlinx.datetime.LocalDateTime
import logic.entities.Project
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun toProjectEntity(projectDto : ProjectDto) : Project{
    return Project(
        id = Uuid.parse(projectDto.id),
        name = projectDto.name,
        description = projectDto.description,
        createdBy = projectDto.createdBy,
        createdAt = LocalDateTime.parse(projectDto.createdAt),
        updatedAt = LocalDateTime.parse(projectDto.updatedAt)
    )
}
@OptIn(ExperimentalUuidApi::class)
fun toProjectDto(project: Project):ProjectDto{
    return ProjectDto(
        id = project.id.toString(),
        name = project.name,
        description = project.description,
        createdBy = project.createdBy,
        createdAt = project.createdAt.toString(),
        updatedAt = project.updatedAt.toString()
    )
}