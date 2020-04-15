package pl.michalregulski.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.michalregulski.taskmanager.model.Task
import pl.michalregulski.taskmanager.model.TaskRepository
import pl.michalregulski.taskmanager.utils.ObservableField

class AddEditTaskViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    val task = ObservableField(Task())

    fun setTask(id: Long) = viewModelScope.launch {
        task.set(taskRepository.get(id))
    }

    fun insertTask() = viewModelScope.launch {
        taskRepository.insert(task.get())
    }

}
