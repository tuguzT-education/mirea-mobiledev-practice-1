package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow

public class AllProjects(private val repository: ProjectRepository) {
    public suspend fun allProjects(): DomainResult<Flow<List<Project>>> =
        repository.getAll()
}
