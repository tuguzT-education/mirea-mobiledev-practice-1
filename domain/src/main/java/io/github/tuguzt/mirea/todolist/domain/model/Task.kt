package io.github.tuguzt.mirea.todolist.domain.model

import kotlinx.datetime.Instant

public data class Task(
    val id: String,
    val name: String,
    val content: String,
    val completed: Boolean,
    val due: TaskDue?,
    val createdAt: Instant,
)

public data class TaskDue(
    val string: String,
    val datetime: Instant,
)

public data class UpdateTask(
    val name: String? = null,
    val content: String? = null,
    val completed: Boolean? = null,
    val due: UpdateTaskDue? = null,
)

public data class UpdateTaskDue(
    val string: String? = null,
    val datetime: Instant? = null,
)
