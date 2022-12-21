package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult

public interface ReopenTask {
    public suspend fun reopenTask(id: String): DomainResult<Unit>
}
