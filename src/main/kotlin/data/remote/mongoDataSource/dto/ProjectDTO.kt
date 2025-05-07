package data.remote.mongoDataSource.dto

data class ProjectDTO(
    val id: String? = null,
    val name: String,
    val description: String,
    val createdBy: String,
    val createdAt: String,
    val updatedAt: String
)