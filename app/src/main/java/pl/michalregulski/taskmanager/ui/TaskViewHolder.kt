package pl.michalregulski.taskmanager.ui

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import pl.michalregulski.taskmanager.R
import pl.michalregulski.taskmanager.model.Task
import pl.michalregulski.taskmanager.utils.formatAsDueDate

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val titleTV: TextView = itemView.findViewById(R.id.taskTitleTV)
    private val dueDateTV: TextView = itemView.findViewById(R.id.taskDueDateTV)
    private val iconIV: AppCompatImageView = itemView.findViewById(R.id.taskIconIV)
    private val doneTV: TextView = itemView.findViewById(R.id.doneTV)

    fun bindTo(task: Task, onClickCallback: (Task) -> Unit) {
        val icon = ContextCompat.getDrawable(itemView.context, task.type.iconId)
        titleTV.text = task.title
        dueDateTV.text = task.dueDate.formatAsDueDate()
        iconIV.setImageDrawable(icon)
        doneTV.setText(task.status.descriptionId)
        itemView.setOnClickListener {
            onClickCallback(task)
        }
    }

}
