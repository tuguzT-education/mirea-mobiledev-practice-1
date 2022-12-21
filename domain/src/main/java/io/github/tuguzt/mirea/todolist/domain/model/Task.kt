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

public fun UpdateTask.isEmpty(): Boolean =
    name == null && content == null && completed == null && due == null

public fun UpdateTask.isNotEmpty(): Boolean = !isEmpty()

public data class UpdateTaskDue(
    val string: String? = null,
    val datetime: Instant? = null,
)

public fun UpdateTaskDue.isEmpty(): Boolean =
    string == null && datetime == null

public fun UpdateTaskDue.isNotEmpty(): Boolean = !isEmpty()
