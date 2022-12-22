package io.github.tuguzt.mirea.todolist.domain.model

public data class Project(
    override val id: Id<Project>,
    val name: String,
    val tasks: List<Task>,
) : Node

public data class CreateProject(val name: String)

public data class UpdateProject(val name: String)
