package data.repository.mapper

import data.dto.ProjectDTO
import kotlinx.datetime.LocalDateTime
import logic.entities.Project
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun toProjectEntity(projectDto: ProjectDTO): Project {
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
fun toProjectDto(project: Project): ProjectDTO {
    return ProjectDTO(
        id = project.id.toString(),
        name = project.name,
        description = project.description,
        createdBy = project.createdBy,
        createdAt = project.createdAt.toString(),
        updatedAt = project.updatedAt.toString()
    )
}