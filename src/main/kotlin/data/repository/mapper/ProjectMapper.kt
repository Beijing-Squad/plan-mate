package data.repository.mapper

import data.remote.mongoDataSource.dto.ProjectDto
import kotlinx.datetime.LocalDateTime
import logic.entity.Project
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun ProjectDto.toProjectEntity(): Project {
    return Project(
        id = Uuid.parse(this.id),
        name = this.name,
        description = this.description,
        createdBy = this.createdBy,
        createdAt = LocalDateTime.parse(this.createdAt),
        updatedAt = LocalDateTime.parse(this.updatedAt)
    )
}

@OptIn(ExperimentalUuidApi::class)
fun Project.toProjectDto(): ProjectDto {
    return ProjectDto(
        id = this.id.toString(),
        name = this.name,
        description = this.description,
        createdBy = this.createdBy,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )
}