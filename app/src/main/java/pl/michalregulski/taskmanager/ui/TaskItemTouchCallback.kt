package pl.michalregulski.taskmanager.ui

import android.content.res.Resources.NotFoundException
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import pl.michalregulski.taskmanager.R
import pl.michalregulski.taskmanager.model.TaskStatus
import pl.michalregulski.taskmanager.viewmodel.TaskListViewModel

class TaskItemTouchCallback(
    private val coordinatorLayout: CoordinatorLayout,
    private val viewModel: TaskListViewModel,
    private val taskAdapter: TaskAdapter // I'm not sure about if these references won't cause memory leaks
) : ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {

    private val deleteIcon: Drawable =
        ContextCompat.getDrawable(coordinatorLayout.context, R.drawable.ic_delete_black_24dp)
            ?: throw NotFoundException()

    private val doneIcon: Drawable =
        ContextCompat.getDrawable(coordinatorLayout.context, R.drawable.ic_baseline_done_24)
            ?: throw NotFoundException()

    private val inProgressIcon: Drawable =
        ContextCompat.getDrawable(coordinatorLayout.context, R.drawable.ic_baseline_redo_24)
            ?: throw NotFoundException()

    private val iconTint: Int =
        ContextCompat.getColor(coordinatorLayout.context, R.color.onPrimaryColor)

    private val removalBackgroundColor: Int =
        ContextCompat.getColor(coordinatorLayout.context, R.color.red)

    private val completionBackgroundColor: Int =
        ContextCompat.getColor(coordinatorLayout.context, R.color.green)

    private val undoCompletionBackgroundColor: Int =
        ContextCompat.getColor(coordinatorLayout.context, R.color.orange)

    private val background: ColorDrawable = ColorDrawable()
    private var icon: Drawable = VectorDrawable()

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
        val itemView = viewHolder.itemView as CardView

        when {
            dX > 0 -> { // Swiping to the right
                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2
                val iconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
                val iconBottom = iconTop + deleteIcon.intrinsicHeight
                val iconLeft = itemView.left + iconMargin
                val iconRight = iconLeft + deleteIcon.intrinsicWidth

                background.apply {
                    color = removalBackgroundColor
                    setBounds(
                        itemView.left,
                        itemView.top,
                        itemView.left + dX.toInt() + itemView.contentPaddingRight,
                        itemView.bottom
                    )
                }

                deleteIcon.apply {
                    setTint(iconTint)
                    setBounds(iconLeft, iconTop, iconRight, iconBottom)
                }

                background.draw(c)
                deleteIcon.draw(c)
            }
            dX < 0 -> { // Swiping to the left
                if (recyclerView.itemAnimator?.isRunning == false) {
                    val task = taskAdapter.getTaskAt(viewHolder.adapterPosition)

                    val (currentColor, currentIcon) = when (task.status) {
                        TaskStatus.DONE -> undoCompletionBackgroundColor to inProgressIcon
                        TaskStatus.IN_PROGRESS -> completionBackgroundColor to doneIcon
                    }

                    val iconMargin = (itemView.height - currentIcon.intrinsicHeight) / 2
                    val iconTop = itemView.top + (itemView.height - currentIcon.intrinsicHeight) / 2
                    val iconBottom = iconTop + currentIcon.intrinsicHeight
                    val iconLeft = itemView.right - iconMargin - currentIcon.intrinsicWidth
                    val iconRight = itemView.right - iconMargin

                    background.apply {
                        color = currentColor
                        setBounds(
                            itemView.right + dX.toInt() - itemView.contentPaddingLeft,
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                        )
                    }

                    currentIcon.apply {
                        setTint(iconTint)
                        setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        draw(c)
                    }

                    icon = currentIcon
                }

                background.draw(c)
                icon.draw(c)
            }
            else -> { // view is unSwiped
                background.setBounds(0, 0, 0, 0)
            }
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val task = taskAdapter.getTaskAt(viewHolder.adapterPosition)
        when (direction) {
            LEFT -> {
                viewModel.changeTaskStatus(task)
                taskAdapter.notifyItemChanged(viewHolder.adapterPosition)
            }
            RIGHT -> {
                viewModel.deleteTask(task)
                Snackbar.make(
                    coordinatorLayout,
                    R.string.msg_task_removed,
                    Snackbar.LENGTH_LONG
                ).apply {
                    setAction(R.string.action_undo) { viewModel.undoDeleteTask() }
                }.show()
            }
        }
    }

}
