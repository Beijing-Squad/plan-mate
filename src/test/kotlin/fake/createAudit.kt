package fake

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import logic.entities.Audit
import logic.entities.type.UserRole
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createAudit(
    userRole: UserRole = UserRole.MATE,
    userName: String = "defaultUser",
    entityId: String = "defaultEntityId",
    entityType: Audit.EntityType = Audit.EntityType.PROJECT,
    action: Audit.ActionType = Audit.ActionType.CREATE,
    actionDetails : String = "",
    timeStamp: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
): Audit {
    return Audit(
        id = Uuid.random(),
        userRole = userRole,
        userName = userName,
        entityId = entityId,
        entityType = entityType,
        action = action,
        actionDetails = actionDetails,
        timeStamp = timeStamp
    )
}