package data.repository.mapper

import data.remote.mongoDataSource.dto.UserDto
import logic.entities.User
import logic.entities.type.UserRole
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun toUserEntity (userDTO: UserDto): User{
    return User(
        id = Uuid.parse(userDTO.id),
        userName = userDTO.userName,
        password = userDTO.password,
        role = UserRole.valueOf(userDTO.role),
    )
}