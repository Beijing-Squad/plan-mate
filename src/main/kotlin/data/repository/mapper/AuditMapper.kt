package data.repository.mapper

import data.remote.mongoDataSource.dto.AuditDto
import kotlinx.datetime.LocalDateTime
import logic.entities.ActionType
import logic.entities.Audit
import logic.entities.EntityType
import logic.entities.type.UserRole
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun toAuditEntity(audit: AuditDto): Audit {
    return Audit(
        id = Uuid.parse(audit.id),
        userRole = UserRole.valueOf(audit.userRole),
        userName = audit.userName,
        entityId = audit.entityId,
        entityType = EntityType.valueOf(audit.entityType),
        actionDetails = audit.actionDetails,
        timeStamp = LocalDateTime.parse(audit.timeStamp),
        action = ActionType.valueOf(audit.action)
    )
}

@OptIn(ExperimentalUuidApi::class)
fun toAuditDTO(audit: Audit): AuditDto {
    return AuditDto(
        id = audit.id.toString(),
        userRole = audit.userRole.toString(),
        userName = audit.userName,
        entityId = audit.entityId,
        entityType = audit.entityType.toString(),
        action = audit.action.toString(),
        actionDetails = audit.actionDetails,
        timeStamp = audit.timeStamp.toString()
    )
}

