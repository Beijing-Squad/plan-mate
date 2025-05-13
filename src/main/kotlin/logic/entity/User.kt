package logic.entity

import logic.entity.type.UserRole
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class User (
    val id: Uuid = Uuid.random(),
    val userName: String,
    val password: String,
    val role: UserRole
)