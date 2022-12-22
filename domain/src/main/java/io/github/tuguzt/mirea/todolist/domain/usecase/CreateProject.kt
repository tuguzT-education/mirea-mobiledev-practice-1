package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.CreateProject
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.repository.ProjectRepository

public class CreateProject(private val repository: ProjectRepository) {
    public suspend fun createProject(create: CreateProject): DomainResult<Project> =
        repository.create(create)
}
