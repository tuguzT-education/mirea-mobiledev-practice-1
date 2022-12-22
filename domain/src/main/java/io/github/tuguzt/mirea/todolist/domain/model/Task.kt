package io.github.tuguzt.mirea.todolist.domain.model

import kotlinx.datetime.Instant

public data class Task(
    override val id: Id<Task>,
    val name: String,
    val content: String,
    val completed: Boolean,
    val due: TaskDue?,
    val createdAt: Instant,
) : Node

public data class CreateTask(
    val project: Id<Project>,
    val name: String,
    val content: String,
)

public data class UpdateTask(
    val name: String? = null,
    val content: String? = null,
    val completed: Boolean? = null,
    val due: UpdateTaskDue? = null,
)

public fun UpdateTask.hasNoUpdates(): Boolean =
    name == null && content == null && completed == null && due == null

public fun UpdateTask.hasUpdates(): Boolean = !hasNoUpdates()
