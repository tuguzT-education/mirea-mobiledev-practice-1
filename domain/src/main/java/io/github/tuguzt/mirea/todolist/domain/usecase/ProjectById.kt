package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.repository.ProjectRepository

public class ProjectById(private val repository: ProjectRepository) {
    public suspend fun projectById(id: Id<Project>): DomainResult<Project?> =
        repository.findById(id)
}
