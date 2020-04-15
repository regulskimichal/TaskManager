package pl.michalregulski.taskmanager.utils

import java.time.Duration
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private val ONE_DAY: Duration = Duration.ofDays(1)

fun ZonedDateTime.formatAsDueDate(): String {
    val midnightToday: ZonedDateTime = ZonedDateTime.now().with(LocalTime.MIDNIGHT)
    val midnightYesterday: ZonedDateTime = midnightToday.minus(ONE_DAY)
    val midnightTomorrow: ZonedDateTime = midnightToday.plus(ONE_DAY)
    val midnightOvermorrow: ZonedDateTime = midnightTomorrow.plus(ONE_DAY)
    val date: String = toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
    val time: String = toLocalTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

    return when {
        midnightYesterday <= this && this < midnightToday -> "Yesterday at $time"
        midnightToday <= this && this < midnightTomorrow -> "Today at $time"
        midnightTomorrow <= this && this < midnightOvermorrow -> "Tomorrow at $time"
        else -> "$date at $time"
    }
}
