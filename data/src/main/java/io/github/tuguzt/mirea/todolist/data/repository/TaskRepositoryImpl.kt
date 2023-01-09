package io.github.tuguzt.mirea.todolist.data.repository

import io.github.tuguzt.mirea.todolist.data.datasource.local.LocalTaskDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.remote.RemoteTaskDataSource
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.Result
import io.github.tuguzt.mirea.todolist.domain.cast
import io.github.tuguzt.mirea.todolist.domain.model.*
import io.github.tuguzt.mirea.todolist.domain.repository.TaskRepository
import io.github.tuguzt.mirea.todolist.domain.success
import kotlinx.coroutines.flow.Flow

public class TaskRepositoryImpl(
    private val remoteDataSource: RemoteTaskDataSource,
    private val localDataSource: LocalTaskDataSource,
) : TaskRepository {

    override suspend fun getAll(project: Id<Project>): DomainResult<Flow<List<Task>>> =
        localDataSource.getAll(project)

    override suspend fun refreshAll(project: Id<Project>): DomainResult<Unit> {
        val tasks = when (val result = remoteDataSource.getAll(project)) {
            is Result.Error -> return result.cast()
            is Result.Success -> result.data
        }
        when (val result = localDataSource.saveAll(project, tasks)) {
            is Result.Error -> return result.cast()
            is Result.Success -> result.data
        }
        return success(Unit)
    }

    override suspend fun findById(id: Id<Task>): DomainResult<Flow<Task?>> =
        localDataSource.findById(id)

    override suspend fun refreshById(id: Id<Task>): DomainResult<Unit> {
        val task = when (val result = remoteDataSource.findById(id)) {
            is Result.Error -> return result.cast()
            is Result.Success -> result.data
        }
        task ?: return localDataSource.delete(id)
        when (val result = localDataSource.save(project = null, task)) {
            is Result.Error -> return result.cast()
            is Result.Success -> result.data
        }
        return success(Unit)
    }

    override suspend fun create(create: CreateTask): DomainResult<Task> {
        val newTask = when (val result = remoteDataSource.create(create)) {
            is Result.Error -> return result
            is Result.Success -> result.data
        }
        val task = when (val result = localDataSource.save(create.project, newTask)) {
            is Result.Error -> return result
            is Result.Success -> result.data
        }
        return success(task)
    }

    override suspend fun update(id: Id<Task>, update: UpdateTask): DomainResult<Task> {
        val updatedTask = when (val result = remoteDataSource.update(id, update)) {
            is Result.Error -> return result
            is Result.Success -> result.data
        }
        val task = when (val result = localDataSource.save(project = null, updatedTask)) {
            is Result.Error -> return result
            is Result.Success -> result.data
        }
        return success(task)
    }

    override suspend fun delete(id: Id<Task>): DomainResult<Unit> {
        when (val result = remoteDataSource.delete(id)) {
            is Result.Error -> return result
            is Result.Success -> result.data
        }
        when (val result = localDataSource.delete(id)) {
            is Result.Error -> return result
            is Result.Success -> result.data
        }
        return success(Unit)
    }
}
