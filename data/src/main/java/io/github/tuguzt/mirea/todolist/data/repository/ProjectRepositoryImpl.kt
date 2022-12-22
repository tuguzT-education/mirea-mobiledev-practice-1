package io.github.tuguzt.mirea.todolist.data.repository

import io.github.tuguzt.mirea.todolist.data.datasource.ProjectDataSource
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.CreateProject
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.UpdateProject
import io.github.tuguzt.mirea.todolist.domain.repository.ProjectRepository

public class ProjectRepositoryImpl(private val dataSource: ProjectDataSource) : ProjectRepository {
    override suspend fun getAll(): DomainResult<List<Project>> =
        dataSource.getAll()

    override suspend fun findById(id: Id<Project>): DomainResult<Project?> =
        dataSource.findById(id)

    override suspend fun create(create: CreateProject): DomainResult<Project> =
        dataSource.create(create)

    override suspend fun update(id: Id<Project>, update: UpdateProject): DomainResult<Project> =
        dataSource.update(id, update)

    override suspend fun delete(id: Id<Project>): DomainResult<Unit> =
        dataSource.delete(id)
}
