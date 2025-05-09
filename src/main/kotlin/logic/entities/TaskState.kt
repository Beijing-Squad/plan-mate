package logic.entities

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class TaskState (
    val id: Uuid = Uuid.random(),
    val name: String,
    val projectId: Uuid
)