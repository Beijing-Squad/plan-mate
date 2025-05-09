package data.repository.mapper

import data.dto.ProjectDTO
import logic.entities.Project
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
fun toProjectEntity(projectDto : ProjectDTO) : Project{
    return Project(
        id = projectDto.id,
        name = projectDto.name,
        description = projectDto.description,
        createdBy = projectDto.createdBy,
        createdAt = projectDto.createdAt,
        updatedAt = projectDto.updatedAt
    )
}
@OptIn(ExperimentalUuidApi::class)
fun toProjectDto(project: Project):ProjectDTO{
    return ProjectDTO(
        id = project.id,
        name = project.name,
        description = project.description,
        createdBy = project.createdBy,
        createdAt = project.createdAt,
        updatedAt = project.updatedAt
    )
}