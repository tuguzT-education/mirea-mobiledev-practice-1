package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.model.Task

public interface CloseTask {
    public suspend fun closeTask(task: Task)
}
