package io.github.tuguzt.mirea.todolist.data.interactor

import io.github.tuguzt.mirea.todolist.data.repository.ProjectRepository
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.usecase.DeleteProject

public class DeleteProjectInteractor(private val repository: ProjectRepository) : DeleteProject {
    override suspend fun deleteProject(project: Project): DomainResult<Unit> =
        repository.delete(project)
}
