package data.repository.mapper

import data.dto.UserDTO
import logic.entities.User
import logic.entities.UserRole
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun toUserEntity(userDTO: UserDTO): User{
    return User(
        id = Uuid.parse(userDTO.id ?: "ba0e1b3c-2239-4755-97fe-202d8619bd79"),
        userName = userDTO.userName,
        password = userDTO.password,
        role = UserRole.valueOf(userDTO.role)
    )
}

@OptIn(ExperimentalUuidApi::class)
fun toUserDto(user: User): UserDTO{
    return UserDTO(
        id = user.id.toString(),
        userName = user.userName,
        password = user.password,
        role = user.role.name

    )
}