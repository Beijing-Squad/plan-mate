package data.dto

data class TaskDTO(
    val id: String? = null,
    val projectId: String,
    val title: String,
    val description: String,
    val createdBy: String,
    val stateId: String,
    val createdAt: String,
    val updatedAt: String
)
