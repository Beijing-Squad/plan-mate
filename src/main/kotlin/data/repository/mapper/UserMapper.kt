package data.repository.mapper

import data.remote.mongoDataSource.dto.UserDto
import logic.entities.User
import kotlin.uuid.ExperimentalUuidApi


@OptIn(ExperimentalUuidApi::class)
fun User.toUserDto(): UserDto {
    return UserDto(
        id = this.id.toString(),
        userName = this.userName,
        password = this.password,
        role = this.role.name

    )
}