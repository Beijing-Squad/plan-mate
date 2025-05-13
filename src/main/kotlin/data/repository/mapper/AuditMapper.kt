package data.repository.mapper

import data.remote.mongoDataSource.dto.AuditDto
import kotlinx.datetime.LocalDateTime
import logic.entity.Audit
import logic.entity.type.UserRole
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun AuditDto.toAuditEntity(): Audit {
    return Audit(
        id = Uuid.parse(this.id),
        userRole = UserRole.valueOf(this.userRole),
        userName = this.userName,
        entityId = this.entityId,
        entityType = Audit.EntityType.valueOf(this.entityType),
        actionDetails = this.actionDetails,
        timeStamp = LocalDateTime.parse(this.timeStamp),
        action = Audit.ActionType.valueOf(this.action)
    )
}

@OptIn(ExperimentalUuidApi::class)
fun Audit.toAuditDto(): AuditDto {
    return AuditDto(
        id = this.id.toString(),
        userRole = this.userRole.toString(),
        userName = this.userName,
        entityId = this.entityId,
        entityType = this.entityType.toString(),
        action = this.action.toString(),
        actionDetails = this.actionDetails,
        timeStamp = this.timeStamp.toString()
    )
}

