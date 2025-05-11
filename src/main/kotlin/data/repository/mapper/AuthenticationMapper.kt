package data.repository.mapper

import data.dto.UserDto
import logic.entities.User
import logic.entities.UserRole
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun UserDto.toUserEntity(): User {
    return User(
        id = Uuid.parse(this.id),
        userName = this.userName,
        password = this.password,
        role = UserRole.valueOf(this.role),
    )
}