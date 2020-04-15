package pl.michalregulski.taskmanager.utils.databinding

import android.content.Context
import androidx.databinding.InverseMethod
import pl.michalregulski.taskmanager.model.TaskType

object TaskTypeConverter {

    @JvmStatic
    @InverseMethod("toTaskType")
    fun fromTaskType(context: Context, value: TaskType): String {
        return context.getString(value.descriptionId)
    }

    @JvmStatic
    fun toTaskType(context: Context, value: String): TaskType {
        return when (value) {
            context.getString(TaskType.TODO.descriptionId) -> TaskType.TODO
            context.getString(TaskType.EMAIL.descriptionId) -> TaskType.EMAIL
            context.getString(TaskType.CALL.descriptionId) -> TaskType.CALL
            context.getString(TaskType.MEETING.descriptionId) -> TaskType.MEETING
            else -> TaskType.TODO
        }
    }

}
