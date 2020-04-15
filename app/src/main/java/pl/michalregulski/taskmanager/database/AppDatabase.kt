package pl.michalregulski.taskmanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.michalregulski.taskmanager.model.Task
import pl.michalregulski.taskmanager.model.TaskRepository
import pl.michalregulski.taskmanager.model.TaskStatus
import pl.michalregulski.taskmanager.model.TaskType
import pl.michalregulski.taskmanager.utils.room.TaskStatusConverter
import pl.michalregulski.taskmanager.utils.room.TaskTypeConverter
import pl.michalregulski.taskmanager.utils.room.ZonedDateTimeConverter
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(ZonedDateTimeConverter::class, TaskTypeConverter::class, TaskStatusConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class InitializationCallback(
        private val taskRepository: Lazy<TaskRepository>
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            GlobalScope.launch {
                TEST_TASKS.forEach {
                    taskRepository.value.insert(it)
                }
            }
        }

        companion object {
            private val todayMidnight: ZonedDateTime =
                ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)

            private val TEST_TASKS = listOf(
                Task(
                    1,
                    "Phone call",
                    "Talk with a group",
                    todayMidnight,
                    TaskStatus.IN_PROGRESS,
                    TaskType.CALL
                ),
                Task(
                    2,
                    "Meeting",
                    "Meet with a group",
                    todayMidnight.plusDays(
                        1
                    ),
                    TaskStatus.IN_PROGRESS,
                    TaskType.MEETING
                ),
                Task(
                    3,
                    "Dinner",
                    "Lasagne",
                    todayMidnight.minusDays(
                        1
                    ).withHour(17),
                    TaskStatus.DONE,
                    TaskType.TODO
                )
            )
        }
    }

}
