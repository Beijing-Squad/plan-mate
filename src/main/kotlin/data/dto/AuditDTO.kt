package data.dto

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty


data class AuditDTO(
    @BsonId val id: String,
    @BsonProperty("userRole") val userRole: String,
    @BsonProperty("userName") val userName: String,
    @BsonProperty("entityId") val entityId: String,
    @BsonProperty("entityType") val entityType: String,
    @BsonProperty("action") val action: String,
    @BsonProperty("actionDetails") val actionDetails: String?,
    @BsonProperty("timeStamp") val timeStamp: String
)