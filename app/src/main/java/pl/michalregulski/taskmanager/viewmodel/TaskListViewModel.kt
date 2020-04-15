package pl.michalregulski.taskmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.michalregulski.taskmanager.model.Task
import pl.michalregulski.taskmanager.model.TaskRepository
import pl.michalregulski.taskmanager.model.TaskStatus
import pl.michalregulski.taskmanager.utils.ObservableBoolean

class TaskListViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private var recentlyRemoved: Task? = null

    val tasks: LiveData<List<Task>> = taskRepository.getAll().asLiveData()

    val isOpened = ObservableBoolean(false)

    fun changeTaskStatus(task: Task) = viewModelScope.launch {
        when (task.status) {
            TaskStatus.DONE -> task.status = TaskStatus.IN_PROGRESS
            TaskStatus.IN_PROGRESS -> task.status = TaskStatus.DONE
        }

        taskRepository.insert(task)
    }

    fun undoDeleteTask() = viewModelScope.launch {
        recentlyRemoved?.let { taskRepository.insert(it) }
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        recentlyRemoved = task
        taskRepository.delete(task)
    }

}
