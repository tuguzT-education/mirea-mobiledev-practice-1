package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.repository.ProjectRepository

public class RefreshProjectById(private val repository: ProjectRepository) {
    public suspend fun refreshProjectById(id: Id<Project>): DomainResult<Unit> =
        repository.refreshById(id)
}
