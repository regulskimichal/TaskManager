package pl.michalregulski.taskmanager.utils.databinding

import androidx.databinding.InverseMethod
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object ZonedDateTimeConverter {

    private val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        .withZone(ZoneId.systemDefault())

    @JvmStatic
    @InverseMethod("toZonedDateTime")
    fun fromZonedDateTime(value: ZonedDateTime?): String {
        return value?.let { dateTimeFormatter.format(it) } ?: ""
    }

    @JvmStatic
    fun toZonedDateTime(value: String): ZonedDateTime? {
        return dateTimeFormatter.parse(value, ZonedDateTime::from)
    }

}
