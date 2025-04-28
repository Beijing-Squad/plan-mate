package logic.entities

import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Task(
    val id: Uuid = Uuid.random(),
    val projectId: String,
    val title: String,
    val description: String,
    val createdBy: String,
    val stateId: String,
    val createdAt: LocalDate,
    val updatedAt: LocalDate
)