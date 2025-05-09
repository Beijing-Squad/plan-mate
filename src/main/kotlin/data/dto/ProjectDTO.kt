package data.dto

import kotlinx.datetime.LocalDateTime

data class ProjectDTO (
    val id: String,
    val name: String,
    val description: String,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)