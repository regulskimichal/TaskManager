package pl.michalregulski.taskmanager.utils.room

import androidx.room.TypeConverter
import pl.michalregulski.taskmanager.model.TaskStatus

object TaskStatusConverter {

    @TypeConverter
    @JvmStatic
    fun toTaskStatus(value: String) = enumValueOf<TaskStatus>(value)

    @TypeConverter
    @JvmStatic
    fun fromTaskStatus(value: TaskStatus) = value.name

}
