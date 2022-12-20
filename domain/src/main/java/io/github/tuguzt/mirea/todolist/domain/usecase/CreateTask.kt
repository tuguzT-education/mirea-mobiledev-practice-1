package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task

public interface CreateTask {
    public suspend fun createTask(parent: Project, name: String, content: String): Task
}
