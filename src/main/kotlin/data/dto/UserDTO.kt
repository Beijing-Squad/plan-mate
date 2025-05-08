package data.dto

import logic.entities.UserRole

data class UserDTO(
    val id: String,
    val userName: String,
    val password: String,
    val role: String
)