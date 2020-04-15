package pl.michalregulski.taskmanager.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.getSystemService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.michalregulski.taskmanager.R
import pl.michalregulski.taskmanager.databinding.ActivityAddEditTaskBinding
import pl.michalregulski.taskmanager.model.TaskType
import pl.michalregulski.taskmanager.utils.update
import pl.michalregulski.taskmanager.viewmodel.AddEditTaskViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class AddEditTaskActivity : AppCompatActivity() {

    private val viewModel by viewModel<AddEditTaskViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAddEditTaskBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@AddEditTaskActivity
            viewModel = this@AddEditTaskActivity.viewModel
        }

        setContentView(binding.root)
        setupSupportActionBar(binding.toolbar)
        setupFloatingActionButton(binding.fab)
        setupFocusListeners(binding.root, binding.content.root)
        setupDateTimePicker(binding.content.dueDateET)
        setupTaskTypePicker(binding.content.taskTypeET)
        prepareData()
    }

    private fun setupSupportActionBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setupFloatingActionButton(fab: FloatingActionButton) {
        fab.setOnClickListener {
            viewModel.insertTask()
            finish()
        }
    }

    private fun setupFocusListeners(root: View, content: View) {
        content.isSoundEffectsEnabled = false

        root.viewTreeObserver.addOnGlobalFocusChangeListener { oldFocusView, newFocusView ->
            if (newFocusView == null || (newFocusView != oldFocusView && !(newFocusView.id == R.id.titleET || newFocusView.id == R.id.descriptionET))) {
                // https://medium.com/@rmirabelle/the-android-sdk-is-the-worst-thing-on-earth-c3aaebbd2e6d
                // Where is something like Keyboard.hide()!?, why do I need to care about window
                // and stupid flag which is not even an Enum...
                val imm = getSystemService<InputMethodManager>()
                imm?.hideSoftInputFromWindow(
                    newFocusView?.windowToken ?: oldFocusView?.windowToken ?: root.windowToken, 0
                )
            }
        }
    }

    private fun setupDateTimePicker(editText: TextInputEditText) {
        editText.showSoftInputOnFocus = false

        editText.setOnFocusChangeListener { v, hasFocus ->
            val task = viewModel.task.get()
            val time: LocalTime = if (task.id != 0L) task.dueDate.toLocalTime() else LocalTime.now()
            val date: LocalDate = if (task.id != 0L) task.dueDate.toLocalDate() else LocalDate.now()

            val onDateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val onTimeSetListener =
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            viewModel.task.update {
                                dueDate = ZonedDateTime.of(
                                    year, month + 1, dayOfMonth,
                                    hourOfDay, minute, 0, 0,
                                    ZoneId.systemDefault()
                                )
                            }
                        }

                    TimePickerDialog(
                        this,
                        onTimeSetListener,
                        time.hour,
                        time.minute,
                        DateFormat.is24HourFormat(this)
                    ).show()
                }

            if (hasFocus) {
                DatePickerDialog(
                    this,
                    onDateSetListener,
                    date.year,
                    date.monthValue - 1,
                    date.dayOfMonth
                ).show()
                v.clearFocus()
            }
        }
    }

    private fun setupTaskTypePicker(taskTypeET: AutoCompleteTextView) {
        taskTypeET.showSoftInputOnFocus = false
        taskTypeET.isSoundEffectsEnabled = false
        taskTypeET.setAdapter(TaskTypeAdapter(this))
    }

    private fun prepareData() {
        val id: Long? = intent.getSerializableExtra(EXTRA_ID) as Long?
        if (id != null) {
            viewModel.setTask(id)
        } else {
            val taskType: TaskType = intent.getSerializableExtra(EXTRA_TASK_TYPE) as TaskType
            viewModel.task.update { type = taskType }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_ID = "pl.michalregulski.taskmanager.EXTRA_ID"
        const val EXTRA_TASK_TYPE = "pl.michalregulski.taskmanager.TASK_TYPE"
    }

}
