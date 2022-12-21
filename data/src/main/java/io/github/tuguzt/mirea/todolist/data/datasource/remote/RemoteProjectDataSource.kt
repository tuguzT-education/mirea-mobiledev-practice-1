package io.github.tuguzt.mirea.todolist.data.datasource.remote

import io.github.tuguzt.mirea.todolist.data.datasource.ProjectDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.remote.model.ApiCreateProject
import io.github.tuguzt.mirea.todolist.data.datasource.remote.model.ApiProject
import io.github.tuguzt.mirea.todolist.data.datasource.remote.model.ApiTask
import io.github.tuguzt.mirea.todolist.data.datasource.remote.model.toResult
import io.github.tuguzt.mirea.todolist.domain.*
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.UpdateProject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

public class RemoteProjectDataSource(apiClient: ApiClient) : ProjectDataSource {
    private val projectApi = apiClient.projectApi
    private val taskApi = apiClient.taskApi

    override suspend fun getAll(): DomainResult<List<Project>> =
        when (val projectsResult = projectApi.all().toResult()) {
            is Result.Error -> projectsResult.cast()
            is Result.Success -> {
                val results = projectsResult.data.pmap { project -> project.toDomain() }
                val projects = results.map { result ->
                    when (result) {
                        is Result.Error -> return result.cast()
                        is Result.Success -> result.data
                    }
                }
                success(projects)
            }
        }

    override suspend fun findById(id: String): DomainResult<Project?> =
        when (val projectResult = projectApi.find(id).toResult()) {
            is Result.Error -> projectResult.cast()
            is Result.Success -> when (val toDomainResult = projectResult.data.toDomain()) {
                is Result.Error -> toDomainResult.cast()
                is Result.Success -> success(toDomainResult.data)
            }
        }

    override suspend fun create(name: String): DomainResult<Project> =
        when (val projectResult = projectApi.create(create = ApiCreateProject(name)).toResult()) {
            is Result.Error -> projectResult.cast()
            is Result.Success -> when (val toDomainResult = projectResult.data.toDomain()) {
                is Result.Error -> toDomainResult.cast()
                is Result.Success -> success(toDomainResult.data)
            }
        }

    override suspend fun update(id: String, update: UpdateProject): DomainResult<Project> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: String): DomainResult<Unit> =
        projectApi.delete(id).toResult()

    private suspend fun ApiProject.toDomain(): DomainResult<Project> {
        val tasks = taskApi.all(id).toResult().map { tasks ->
            tasks.map(ApiTask::toDomain)
        }
        return tasks.map {
            Project(id = id, name = name, tasks = it)
        }
    }
}

internal suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}
