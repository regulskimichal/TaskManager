package pl.michalregulski.taskmanager.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.michalregulski.taskmanager.databinding.ActivityTaskListBinding
import pl.michalregulski.taskmanager.model.TaskType
import pl.michalregulski.taskmanager.utils.ObservableBoolean
import pl.michalregulski.taskmanager.utils.addOnPropertyChanged
import pl.michalregulski.taskmanager.utils.negate
import pl.michalregulski.taskmanager.viewmodel.TaskListViewModel


class TaskListActivity : AppCompatActivity() {

    private val viewModel by viewModel<TaskListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityTaskListBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@TaskListActivity
            viewModel = this@TaskListActivity.viewModel
        }

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupFabMenu(
            viewModel.isOpened,
            binding.fab,
            listOf(
                FabMenuEntry(binding.addTodoFAB, binding.addTodoTV, TaskType.TODO),
                FabMenuEntry(binding.addEmailFAB, binding.addEmailTV, TaskType.EMAIL),
                FabMenuEntry(binding.addCallFAB, binding.addCallTV, TaskType.CALL),
                FabMenuEntry(binding.addMeetingFAB, binding.addMeetingTV, TaskType.MEETING)
            )
        )
        setupRecyclerView(binding.content.recyclerView, binding.taskListCL)
    }

    private fun setupFabMenu(
        state: ObservableBoolean,
        fab: FloatingActionButton,
        menuEntries: List<FabMenuEntry>
    ) {
        fab.setOnClickListener {
            changeFabMenuState()
        }

        menuEntries.forEach { menuEntry ->
            menuEntry.button.setOnClickListener {
                startActivityAddTask {
                    putExtra(AddEditTaskActivity.EXTRA_TASK_TYPE, menuEntry.taskType)
                }
            }
        }

        state.addOnPropertyChanged {
            menuEntries.forEach { menuEntry ->
                set(menuEntry.button, menuEntry.description, it)
            }
        }
    }

    private fun changeFabMenuState() {
        viewModel.isOpened.negate()
    }

    private fun closeFabMenu() {
        if (viewModel.isOpened.get()) {
            changeFabMenuState()
        }
    }

    private fun set(fab: FloatingActionButton, textView: TextView, visible: Boolean) {
        if (visible) {
            textView.visibility = View.VISIBLE
            textView.animate().setStartDelay(200).setDuration(100).scaleX(1.0f).scaleY(1.0f).start()
            fab.show()
        } else {
            textView.visibility = View.INVISIBLE
            textView.animate().setStartDelay(200).setDuration(100).scaleX(0.0f).scaleY(0.0f).start()
            fab.hide()
        }
    }

    private fun startActivityAddTask(callback: Intent.() -> Unit) {
        Intent(
            this,
            AddEditTaskActivity::class.java
        ).apply {
            callback()
        }.also {
            startActivity(it)
            closeFabMenu()
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, parent: CoordinatorLayout) {
        recyclerView.apply {
            val taskAdapter = TaskAdapter { task ->
                startActivityAddTask {
                    putExtra(AddEditTaskActivity.EXTRA_ID, task.id)
                }
            }

            viewModel.tasks.observe(this@TaskListActivity, Observer { taskAdapter.submitList(it) })

            adapter = taskAdapter
            layoutManager = LinearLayoutManager(this@TaskListActivity)
            itemAnimator = DefaultItemAnimator()
            ItemTouchHelper(TaskItemTouchCallback(parent, viewModel, taskAdapter))
                .also { it.attachToRecyclerView(recyclerView) }
        }
    }

    data class FabMenuEntry(
        val button: FloatingActionButton,
        val description: TextView,
        val taskType: TaskType
    )

}
