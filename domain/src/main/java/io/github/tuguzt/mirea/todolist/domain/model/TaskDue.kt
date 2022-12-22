package io.github.tuguzt.mirea.todolist.domain.model

import kotlinx.datetime.Instant

public data class TaskDue(
    val string: String,
    val datetime: Instant,
)

public data class UpdateTaskDue(
    val string: String? = null,
    val datetime: Instant? = null,
)

public fun UpdateTaskDue.hasNoUpdates(): Boolean =
    string == null && datetime == null

public fun UpdateTaskDue.hasUpdates(): Boolean = !hasNoUpdates()
