package pl.michalregulski.taskmanager.model

import pl.michalregulski.taskmanager.R

enum class TaskType(
    val iconId: Int,
    val descriptionId: Int
) {
    TODO(R.drawable.ic_assignment_turned_in_black_24dp, R.string.title_todo),
    EMAIL(R.drawable.ic_email_black_24dp, R.string.title_email),
    CALL(R.drawable.ic_phone_black_24dp, R.string.title_call),
    MEETING(R.drawable.ic_people_black_24dp, R.string.title_meeting)
}
