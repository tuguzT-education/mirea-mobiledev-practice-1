package io.github.tuguzt.mirea.todolist.domain.model

import kotlinx.datetime.Instant

public data class Task(
    val id: String,
    val name: String,
    val content: String,
    val description: String,
    val completed: Boolean,
    val due: TaskDue?,
    val createdAt: Instant,
)
