package fake

import kotlinx.datetime.LocalDate
import logic.entities.ActionType
import logic.entities.Audit
import logic.entities.EntityType
import logic.entities.UserRole
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createAudit(
    userRole: UserRole = UserRole.MATE,
    userName: String = "defaultUser",
    entityId: String = "defaultEntityId",
    entityType: EntityType = EntityType.PROJECT,
    action: ActionType = ActionType.CREATE,
    oldState: String? = null,
    newState: String? = null,
    timeStamp: LocalDate = LocalDate(2023, 1, 1)
): Audit {
    return Audit(
        id = Uuid.random(),
        userRole = userRole,
        userName = userName,
        entityId = entityId,
        entityType = entityType,
        action = action,
        oldState = oldState,
        newState = newState,
        timeStamp = timeStamp
    )
}