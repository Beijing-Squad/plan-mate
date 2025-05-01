package logic.entities

import kotlin.uuid.ExperimentalUuidApi

data class State @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String,
    val name: String,
    val projectId: String
)