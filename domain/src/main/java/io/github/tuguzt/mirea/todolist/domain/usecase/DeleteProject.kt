package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.repository.ProjectRepository

public class DeleteProject(private val repository: ProjectRepository) {
    public suspend fun deleteProject(id: Id<Project>): DomainResult<Unit> =
        repository.delete(id)
}
