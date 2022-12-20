package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.model.Task

public interface AllTasks {
    public suspend fun allTasks(): List<Task>
}
