package pl.michalregulski.taskmanager.utils.room

import androidx.room.TypeConverter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ZonedDateTimeConverter {

    private val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

    @TypeConverter
    @JvmStatic
    fun toZonedDateTime(string: String?): ZonedDateTime? {
        return string?.let {
            formatter.parse(it, ZonedDateTime::from)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromZonedDateTime(zonedDateTime: ZonedDateTime?): String? {
        return zonedDateTime?.format(formatter)
    }

}
