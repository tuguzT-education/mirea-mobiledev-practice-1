package io.github.tuguzt.mirea.todolist.viewmodel

import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import kotlinx.datetime.Clock

val fakeProjects = mutableListOf(
    Project(
        id = "42",
        name = "Hello World",
        tasks = listOf(),
    ),
    Project(
        id = "43",
        name = "My own project",
        tasks = listOf(
            Task(
                id = "42",
                name = "Buy milk",
                content = "",
                completed = true,
                due = null,
                createdAt = Clock.System.now(),
            ),
            Task(
                id = "43",
                name = "New task",
                content = "## Some `markdown`",
                completed = false,
                due = null,
                createdAt = Clock.System.now(),
            ),
        ),
    ),
    Project(
        id = "44",
        name = "Completed project",
        tasks = listOf(
            Task(
                id = "42",
                name = "Buy milk",
                content = "",
                completed = true,
                due = null,
                createdAt = Clock.System.now(),
            ),
            Task(
                id = "43",
                name = "New task",
                content = "## Some `markdown`",
                completed = true,
                due = null,
                createdAt = Clock.System.now(),
            ),
        ),
    ),
)
