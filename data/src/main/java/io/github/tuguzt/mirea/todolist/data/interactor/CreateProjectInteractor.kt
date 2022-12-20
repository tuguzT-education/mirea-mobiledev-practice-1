package io.github.tuguzt.mirea.todolist.data.interactor

import io.github.tuguzt.mirea.todolist.data.repository.ProjectRepository
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.usecase.CreateProject

public class CreateProjectInteractor(private val repository: ProjectRepository) : CreateProject {
    override suspend fun createProject(name: String): Project =
        repository.create(name, tasks = listOf())
}
