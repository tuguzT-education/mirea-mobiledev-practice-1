package io.github.tuguzt.mirea.todolist.domain.model

import kotlinx.datetime.Instant

public data class TaskDue(
    val string: String,
    val datetime: Instant,
)
