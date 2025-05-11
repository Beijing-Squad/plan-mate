package fake

import logic.entities.User
import logic.entities.type.UserRole
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createUser(
    userName: String = "defaultUser",
    password: String = "defaultPassword",
    role: UserRole = UserRole.MATE
): User {
    return User(
        id = Uuid.random(),
        userName = userName,
        password = password,
        role = role
    )
}