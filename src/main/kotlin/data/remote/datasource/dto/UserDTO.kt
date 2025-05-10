package data.remote.datasource.dto

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty

data class UserDTO(

    @BsonId val id: String,
    @BsonProperty("userName") val userName: String,
    @BsonProperty("password") val password: String,
    @BsonProperty("role") val role: String

)