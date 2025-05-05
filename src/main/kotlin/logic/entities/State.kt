package logic.entities

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class State @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val name: String,
    val projectId: String
)