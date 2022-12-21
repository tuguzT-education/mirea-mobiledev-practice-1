package io.github.tuguzt.mirea.todolist.data.repository

import io.github.tuguzt.mirea.todolist.data.datasource.ProjectDataSource
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task

public class ProjectRepository(private val dataSource: ProjectDataSource) {
    public suspend fun getAll(): DomainResult<List<Project>> =
        dataSource.getAll()

    public suspend fun findById(id: String): DomainResult<Project?> =
        dataSource.findById(id)

    public suspend fun create(name: String): DomainResult<Project> =
        dataSource.create(name)

    public suspend fun update(project: Project): DomainResult<Project> =
        dataSource.update(project)

    public suspend fun delete(project: Project): DomainResult<Unit> =
        dataSource.delete(project)
}
