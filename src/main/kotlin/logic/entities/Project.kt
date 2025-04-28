package logic.entities

import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Project(
    val id: Uuid = Uuid.random(),
    val name: String,
    val description: String,
    val createdBy: String,
    val createdAt: LocalDate,
    val updatedAt: LocalDate
)