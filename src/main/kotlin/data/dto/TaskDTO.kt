package data.dto

import kotlinx.datetime.LocalDateTime

data class TaskDTO(
    val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val createdBy: String,
    val stateId: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
