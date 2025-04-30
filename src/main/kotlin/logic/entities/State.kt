package logic.entities

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class State @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid = Uuid.random(),
    val name: String,
    val projectId: String
)