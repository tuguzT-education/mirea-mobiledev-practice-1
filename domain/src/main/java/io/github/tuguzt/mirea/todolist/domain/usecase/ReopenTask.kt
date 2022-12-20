package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Task

public interface ReopenTask {
    public suspend fun reopenTask(task: Task): DomainResult<Unit>
}
