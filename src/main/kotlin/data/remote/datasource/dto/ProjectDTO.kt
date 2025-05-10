package data.remote.datasource.dto

import kotlinx.datetime.LocalDateTime
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty

data class ProjectDTO(

    @BsonId val id: String,
    @BsonProperty("name") val name: String,
    @BsonProperty("description") val description: String,
    @BsonProperty("createdBy") val createdBy: String,
    @BsonProperty("createdAt") val createdAt: LocalDateTime,
    @BsonProperty("updatedAt") val updatedAt: LocalDateTime

)