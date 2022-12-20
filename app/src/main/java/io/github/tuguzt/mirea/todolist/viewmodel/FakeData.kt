package io.github.tuguzt.mirea.todolist.viewmodel

import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import kotlinx.datetime.Clock
import java.util.*

var fakeProjects = listOf(
    Project(
        id = UUID.randomUUID().toString(),
        name = "Hello World",
        tasks = listOf(),
    ),
    Project(
        id = UUID.randomUUID().toString(),
        name = "My own project",
        tasks = listOf(
            Task(
                id = UUID.randomUUID().toString(),
                name = "Buy milk",
                content = "",
                completed = true,
                due = null,
                createdAt = Clock.System.now(),
            ),
            Task(
                id = UUID.randomUUID().toString(),
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
                id = UUID.randomUUID().toString(),
                name = "Buy milk",
                content = "",
                completed = true,
                due = null,
                createdAt = Clock.System.now(),
            ),
            Task(
                id = UUID.randomUUID().toString(),
                name = "New task",
                content = "## Some `markdown`",
                completed = true,
                due = null,
                createdAt = Clock.System.now(),
            ),
        ),
    ),
)
