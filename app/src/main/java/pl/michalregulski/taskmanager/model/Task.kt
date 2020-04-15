package pl.michalregulski.taskmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var title: String = "",
    var description: String = "",
    var dueDate: ZonedDateTime = ZonedDateTime.now(),
    var status: TaskStatus = TaskStatus.IN_PROGRESS,
    var type: TaskType = TaskType.TODO
)
