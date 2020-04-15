package pl.michalregulski.taskmanager.utils.room

import androidx.room.TypeConverter
import pl.michalregulski.taskmanager.model.TaskType

object TaskTypeConverter {

    @TypeConverter
    @JvmStatic
    fun toTaskType(value: String) = enumValueOf<TaskType>(value)

    @TypeConverter
    @JvmStatic
    fun fromTaskType(value: TaskType) = value.name

}