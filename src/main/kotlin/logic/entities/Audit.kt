package logic.entities

import kotlinx.datetime.LocalDateTime
import logic.entities.type.UserRole
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
    val actionDetails : String?,
    val timeStamp: LocalDateTime
)

enum class ActionType {
    CREATE,
    UPDATE,
    DELETE
}

enum class EntityType {
    PROJECT,
    TASK
}