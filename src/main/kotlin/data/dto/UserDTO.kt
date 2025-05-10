package data.dto

import org.bson.codecs.pojo.annotations.BsonProperty

data class UserDTO(
    @BsonProperty("id") val id: String,
    @BsonProperty("userName") val userName: String,
    @BsonProperty("password") val password: String,
    @BsonProperty("role") val role: String
)