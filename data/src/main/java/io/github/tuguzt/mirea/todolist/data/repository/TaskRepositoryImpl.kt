package io.github.tuguzt.mirea.todolist.data.repository

import io.github.tuguzt.mirea.todolist.data.datasource.TaskDataSource
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.*
import io.github.tuguzt.mirea.todolist.domain.repository.TaskRepository

public class TaskRepositoryImpl(private val dataSource: TaskDataSource) : TaskRepository {
    override suspend fun getAll(project: Id<Project>): DomainResult<List<Task>> =
        dataSource.getAll(project)

    override suspend fun findById(id: Id<Task>): DomainResult<Task?> =
        dataSource.findById(id)

    override suspend fun create(create: CreateTask): DomainResult<Task> =
        dataSource.create(create)

    override suspend fun update(id: Id<Task>, update: UpdateTask): DomainResult<Task> =
        dataSource.update(id, update)

    override suspend fun delete(id: Id<Task>): DomainResult<Unit> =
        dataSource.delete(id)
}
