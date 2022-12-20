package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Project

public interface CreateProject {
    public suspend fun createProject(name: String): DomainResult<Project>
}
