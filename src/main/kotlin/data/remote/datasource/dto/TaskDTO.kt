package data.remote.datasource.dto

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty

data class TaskDTO(

    @BsonId val id: String,
    @BsonProperty("projectId") val projectId: String,
    @BsonProperty("title") val title: String,
    @BsonProperty("description") val description: String,
    @BsonProperty("createdBy") val createdBy: String,
    @BsonProperty("stateId") val stateId: String,
    @BsonProperty("createdAt") val createdAt: String,
    @BsonProperty("updatedAt") val updatedAt: String

)