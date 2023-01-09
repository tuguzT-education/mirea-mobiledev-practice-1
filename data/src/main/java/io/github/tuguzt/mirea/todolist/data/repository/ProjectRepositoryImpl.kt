package io.github.tuguzt.mirea.todolist.data.repository

import io.github.tuguzt.mirea.todolist.data.datasource.local.LocalProjectDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.remote.RemoteProjectDataSource
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.Result
import io.github.tuguzt.mirea.todolist.domain.cast
import io.github.tuguzt.mirea.todolist.domain.model.CreateProject
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.UpdateProject
import io.github.tuguzt.mirea.todolist.domain.repository.ProjectRepository
import io.github.tuguzt.mirea.todolist.domain.success
import kotlinx.coroutines.flow.Flow

public class ProjectRepositoryImpl(
    private val remoteDataSource: RemoteProjectDataSource,
    private val localDataSource: LocalProjectDataSource,
) : ProjectRepository {

    override suspend fun getAll(): DomainResult<Flow<List<Project>>> =
        localDataSource.getAll()

    override suspend fun refreshAll(): DomainResult<Unit> {
        val projects = when (val result = remoteDataSource.getAll()) {
            is Result.Error -> return result.cast()
            is Result.Success -> result.data
        }
        when (val result = localDataSource.saveAll(projects)) {
            is Result.Error -> return result.cast()
            is Result.Success -> result.data
        }
        return success(Unit)
    }

    override suspend fun findById(id: Id<Project>): DomainResult<Flow<Project?>> =
        localDataSource.findById(id)

    override suspend fun refreshById(id: Id<Project>): DomainResult<Unit> {
        val project = when (val result = remoteDataSource.findById(id)) {
            is Result.Error -> return result.cast()
            is Result.Success -> result.data
        }
        project ?: return success(Unit)
        when (val result = localDataSource.save(project)) {
            is Result.Error -> return result.cast()
            is Result.Success -> result.data
        }
        return success(Unit)
    }

    override suspend fun create(create: CreateProject): DomainResult<Project> {
        val newProject = when (val result = remoteDataSource.create(create)) {
            is Result.Error -> return result
            is Result.Success -> result.data
        }
        val project = when (val result = localDataSource.save(newProject)) {
            is Result.Error -> return result
            is Result.Success -> result.data
        }
        return success(project)
    }

    override suspend fun update(id: Id<Project>, update: UpdateProject): DomainResult<Project> {
        val updatedProject = when (val result = remoteDataSource.update(id, update)) {
            is Result.Error -> return result
            is Result.Success -> result.data
        }
        val project = when (val result = localDataSource.save(updatedProject)) {
            is Result.Error -> return result
            is Result.Success -> result.data
        }
        return success(project)
    }

    override suspend fun delete(id: Id<Project>): DomainResult<Unit> {
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
