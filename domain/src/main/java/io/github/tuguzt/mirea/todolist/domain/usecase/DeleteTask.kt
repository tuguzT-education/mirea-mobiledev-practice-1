package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Task

public interface DeleteTask {
    public suspend fun deleteTask(task: Task): DomainResult<Unit>
}
