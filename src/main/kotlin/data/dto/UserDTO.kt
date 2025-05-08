package data.dto

import org.bson.codecs.pojo.annotations.BsonProperty

data class UserDTO(
    val id: String?,
    val userName: String,
    val password: String,
    val role: String
)