// data/remote/mongoDataSource/codecs/LocalDateTimeCodec.kt
package data.remote.mongoDataSource.mongoConnection

import kotlinx.datetime.LocalDateTime
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import java.time.format.DateTimeFormatter

class LocalDateTimeCodec : Codec<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun encode(writer: BsonWriter, value: LocalDateTime, encoderContext: EncoderContext) {
        writer.writeString(value.toString()) // تحويل LocalDateTime إلى String
    }

    override fun decode(reader: BsonReader, decoderContext: DecoderContext): LocalDateTime {
        return LocalDateTime.parse(reader.readString()) // تحويل String إلى LocalDateTime
    }

    override fun getEncoderClass(): Class<LocalDateTime> {
        return LocalDateTime::class.java
    }
}