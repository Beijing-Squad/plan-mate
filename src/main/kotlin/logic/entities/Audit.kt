package logic.entities

import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Audit(
    val id: Uuid = Uuid.random(),
    val userRole: UserRole,
    val userName: String,
    val entityId: String,
    val entityType: EntityType,
    val action: ActionType,
    val oldState: String?,
    val newState: String?,
    val timeStamp: LocalDateTime
)