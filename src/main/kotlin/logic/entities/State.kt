package logic.entities

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class State (
    val id: Uuid = Uuid.random(),
    val name: String,
    val projectId: String
)