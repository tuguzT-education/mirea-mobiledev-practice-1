package io.github.tuguzt.mirea.todolist.data.datasource.remote

import io.github.tuguzt.mirea.todolist.data.datasource.ProjectDataSource
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task

public class RemoteProjectDataSource(apiClient: ApiClient) : ProjectDataSource {
    private val projectApi = apiClient.projectApi

    override suspend fun getAll(): DomainResult<List<Project>> {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: String): DomainResult<Project?> {
        TODO("Not yet implemented")
    }

    override suspend fun create(name: String, tasks: List<Task>): DomainResult<Project> {
        TODO("Not yet implemented")
    }

    override suspend fun update(project: Project): DomainResult<Project> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(project: Project): DomainResult<Unit> {
        TODO("Not yet implemented")
    }
}
