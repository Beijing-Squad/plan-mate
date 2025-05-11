package data.repository.mapper

import data.dto.UserDto
import logic.entities.User
import kotlin.uuid.ExperimentalUuidApi


@OptIn(ExperimentalUuidApi::class)
fun toUserDto(user: User): UserDto {
    return UserDto(
        id = user.id.toString(),
        userName = user.userName,
        password = user.password,
        role = user.role.name

    )
}