package io.github.tuguzt.mirea.todolist.data.interactor

import io.github.tuguzt.mirea.todolist.data.repository.ProjectRepository
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.usecase.AllProjects

public class AllProjectsInteractor(private val repository: ProjectRepository) : AllProjects {
    override suspend fun allProjects(): List<Project> = repository.getAll()
}
