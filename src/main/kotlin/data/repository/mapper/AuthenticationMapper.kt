package data.repository.mapper

import data.dto.UserDTO
import logic.entities.User
import logic.entities.UserRole
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun toUserEntity (userDTO: UserDTO): User{
    return User(
        id = Uuid.parse(userDTO.id),
        userName = userDTO.userName,
        password = userDTO.password,
        role = UserRole.valueOf(userDTO.role),
    )
}