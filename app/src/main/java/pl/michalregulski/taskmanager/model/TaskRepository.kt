package pl.michalregulski.taskmanager.model

import pl.michalregulski.taskmanager.database.TaskDao

class TaskRepository(
    private val taskDao: TaskDao
) {

    fun getAll() = taskDao.getAll()

    suspend fun get(id: Long) = taskDao.get(id)

    suspend fun insert(task: Task) = taskDao.insert(task)

    suspend fun delete(task: Task) = taskDao.delete(task)

}
