package pl.michalregulski.taskmanager.model

import pl.michalregulski.taskmanager.R

enum class TaskStatus(
    val descriptionId: Int
) {
    DONE(R.string.title_done),
    IN_PROGRESS(R.string.title_in_progress)
}
