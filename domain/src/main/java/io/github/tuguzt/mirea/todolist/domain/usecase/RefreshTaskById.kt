package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.repository.TaskRepository

public class RefreshTaskById(private val repository: TaskRepository) {
    public suspend fun refreshTaskById(id: Id<Task>): DomainResult<Unit> =
        repository.refreshById(id)
}
