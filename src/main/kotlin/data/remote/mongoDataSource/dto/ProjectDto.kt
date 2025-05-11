package data.remote.mongoDataSource.dto

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty

data class ProjectDto(
    @BsonId val id: String,
    @BsonProperty("name") val name: String,
    @BsonProperty("description") val description: String,
    @BsonProperty("createdBy") val createdBy: String,
    @BsonProperty("createdAt") val createdAt: String,
    @BsonProperty("updatedAt") val updatedAt: String
)