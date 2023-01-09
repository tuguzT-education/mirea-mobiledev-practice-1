package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.repository.TaskRepository

public class RefreshAllTasks(private val repository: TaskRepository) {
    public suspend fun refreshAllTasks(project: Id<Project>): DomainResult<Unit> =
        repository.refreshAll(project)
}
