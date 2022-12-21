package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult

public interface DeleteTask {
    public suspend fun deleteTask(id: String): DomainResult<Unit>
}
