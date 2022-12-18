package io.github.tuguzt.mirea.todolist.domain.model

public data class Project(
    val id: String,
    val name: String,
    val tasks: List<Task>,
)
