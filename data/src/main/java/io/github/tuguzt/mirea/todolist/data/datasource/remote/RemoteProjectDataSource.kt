package io.github.tuguzt.mirea.todolist.data.datasource.remote

import io.github.tuguzt.mirea.todolist.data.datasource.ProjectDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.remote.model.*
import io.github.tuguzt.mirea.todolist.data.datasource.remote.model.ApiTask
import io.github.tuguzt.mirea.todolist.domain.*
import io.github.tuguzt.mirea.todolist.domain.model.CreateProject
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.UpdateProject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

public class RemoteProjectDataSource(client: ApiClient) : ProjectDataSource {
    private val projectApi = client.projectApi
    private val taskApi = client.taskApi

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

    override suspend fun findById(id: Id<Project>): DomainResult<Project?> =
        when (val projectResult = projectApi.find(id.value).toResult()) {
            is Result.Error -> projectResult.cast()
            is Result.Success -> when (val toDomainResult = projectResult.data.toDomain()) {
                is Result.Error -> toDomainResult.cast()
                is Result.Success -> success(toDomainResult.data)
            }
        }

    override suspend fun create(create: CreateProject): DomainResult<Project> {
        val apiCreate = ApiCreateProject(name = create.name)
        return when (val projectResult = projectApi.create(apiCreate).toResult()) {
            is Result.Error -> projectResult.cast()
            is Result.Success -> when (val toDomainResult = projectResult.data.toDomain()) {
                is Result.Error -> toDomainResult.cast()
                is Result.Success -> success(toDomainResult.data)
            }
        }
    }

    override suspend fun update(id: Id<Project>, update: UpdateProject): DomainResult<Project> {
        val apiUpdate = ApiUpdateProject(name = update.name)
        return when (val projectResult = projectApi.update(id.value, apiUpdate).toResult()) {
            is Result.Error -> projectResult.cast()
            is Result.Success -> when (val toDomainResult = projectResult.data.toDomain()) {
                is Result.Error -> toDomainResult.cast()
                is Result.Success -> success(toDomainResult.data)
            }
        }
    }

    override suspend fun delete(id: Id<Project>): DomainResult<Unit> =
        projectApi.delete(id.value).toResult()

    private suspend fun ApiProject.toDomain(): DomainResult<Project> {
        val tasks = taskApi.all(id).toResult().map { tasks ->
            tasks.map(ApiTask::toDomain)
        }
        return tasks.map {
            Project(id = Id(id), name = name, tasks = it)
        }
    }
}

@Suppress("SpellCheckingInspection")
internal suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> =
    coroutineScope {
        map { async { f(it) } }.awaitAll()
    }
