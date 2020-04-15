package pl.michalregulski.taskmanager.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import pl.michalregulski.taskmanager.R
import pl.michalregulski.taskmanager.model.TaskType

class TaskTypeAdapter(context: Context, val values: Array<TaskType> = TaskType.values()) :
    ArrayAdapter<TaskType>(context, 0, values) {

    //Android SDK things...
    private val _filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            results.values = values
            results.count = values.size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return if (resultValue is TaskType) context.getString(resultValue.descriptionId) else ""
        }

    }

    override fun getFilter(): Filter {
        return _filter
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listItemView: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_task_type, parent, false)

        val taskType: TaskType? = getItem(position)

        val taskTypeName: TextView = listItemView.findViewById(R.id.taskTypeNameTV)
        taskTypeName.setText(taskType?.descriptionId ?: R.string.empty)

        val iconView: ImageView = listItemView.findViewById(R.id.taskTypeIconIV)
        iconView.setImageResource(taskType?.iconId ?: android.R.color.transparent)

        return listItemView
    }

}
