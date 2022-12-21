package io.github.tuguzt.mirea.todolist.domain.model

public data class Project(
    val id: String,
    val name: String,
    val tasks: List<Task>,
)

public data class UpdateProject(val name: String)
