package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.repository.ProjectRepository

public class RefreshAllProjects(private val repository: ProjectRepository) {
    public suspend fun refreshAllProjects(): DomainResult<Unit> =
        repository.refreshAll()
}
