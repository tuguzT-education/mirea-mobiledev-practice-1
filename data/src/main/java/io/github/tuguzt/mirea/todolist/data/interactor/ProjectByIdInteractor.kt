package io.github.tuguzt.mirea.todolist.data.interactor

import io.github.tuguzt.mirea.todolist.data.repository.ProjectRepository
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.usecase.ProjectById

public class ProjectByIdInteractor(private val repository: ProjectRepository) : ProjectById {
    override suspend fun projectById(id: String): Project? = repository.findById(id)
}
