package fake

import kotlinx.datetime.LocalDate
import logic.entities.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createUser(
    userName: String,
    password: String,
    role: UserRole
): User {
    return User(
        id = Uuid.random(),
        userName = userName,
        password = password,
        role = role
    )
}

@OptIn(ExperimentalUuidApi::class)
fun createTask(
    projectId: String,
    title: String,
    description: String,
    createdBy: String,
    stateId: String,
    createdAt: LocalDate,
    updatedAt: LocalDate
): Task {
    return Task(
        id = Uuid.random(),
        projectId = projectId,
        title = title,
        description = description,
        createdBy = createdBy,
        stateId = stateId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun createState(
    id: String,
    name: String,
    projectId: String
): State {
    return State(
        id = id,
        name = name,
        projectId = projectId
    )
}

@OptIn(ExperimentalUuidApi::class)
fun createProject(
    name: String,
    description: String,
    createdBy: String,
    createdAt: LocalDate,
    updatedAt: LocalDate
): Project {
    return Project(
        id = Uuid.random(),
        name = name,
        description = description,
        createdBy = createdBy,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

@OptIn(ExperimentalUuidApi::class)
fun createAudit(
    userRole: UserRole,
    userName: String,
    entityId: String,
    entityType: EntityType,
    action: ActionType,
    oldState: String?,
    newState: String?,
    timeStamp: LocalDate
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