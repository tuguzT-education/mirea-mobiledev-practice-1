package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.model.Task

public interface TaskById {
    public suspend fun taskById(id: String): Task?
}
