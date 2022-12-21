package io.github.tuguzt.mirea.todolist.data.repository

import io.github.tuguzt.mirea.todolist.data.datasource.ProjectDataSource
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.UpdateProject

public class ProjectRepository(private val dataSource: ProjectDataSource) {
    public suspend fun getAll(): DomainResult<List<Project>> =
        dataSource.getAll()

    public suspend fun findById(id: String): DomainResult<Project?> =
        dataSource.findById(id)

    public suspend fun create(name: String): DomainResult<Project> =
        dataSource.create(name)

    public suspend fun update(id: String, update: UpdateProject): DomainResult<Project> =
        dataSource.update(id, update)

    public suspend fun delete(id: String): DomainResult<Unit> =
        dataSource.delete(id)
}
