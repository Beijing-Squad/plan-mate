package data.remote.mongoDataSource.dto

import logic.entities.ActionType
import logic.entities.EntityType
import logic.entities.UserRole

data class AuditDTO(
    val id: String? = null,
    val userRole: UserRole,
    val userName: String,
    val entityId: String,
    val entityType: EntityType,
    val action: ActionType,
    val oldState: String?,
    val newState: String?,
    val timeStamp: String
)