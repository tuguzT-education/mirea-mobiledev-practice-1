package io.github.tuguzt.mirea.todolist.domain.repository

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.*
import kotlinx.coroutines.flow.Flow

public interface TaskRepository {
    public suspend fun getAll(project: Id<Project>): DomainResult<Flow<List<Task>>>

    public suspend fun refreshAll(project: Id<Project>): DomainResult<Unit>

    public suspend fun findById(id: Id<Task>): DomainResult<Flow<Task?>>

    public suspend fun refreshById(id: Id<Task>): DomainResult<Unit>

    public suspend fun create(create: CreateTask): DomainResult<Task>

    public suspend fun update(id: Id<Task>, update: UpdateTask): DomainResult<Task>

    public suspend fun delete(id: Id<Task>): DomainResult<Unit>
}
