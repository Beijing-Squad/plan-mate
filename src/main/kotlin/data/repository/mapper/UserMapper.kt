package data.repository.mapper

import data.dto.UserDTO
import logic.entities.User
import kotlin.uuid.ExperimentalUuidApi


@OptIn(ExperimentalUuidApi::class)
fun toUserDto(user: User): UserDTO {
    return UserDTO(
        id = user.id.toString(),
        userName = user.userName,
        password = user.password,
        role = user.role.name

    )
}