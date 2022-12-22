package io.github.tuguzt.mirea.todolist.domain.repository

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.*

public interface TaskRepository {
    public suspend fun getAll(project: Id<Project>): DomainResult<List<Task>>

    public suspend fun findById(id: Id<Task>): DomainResult<Task?>

    public suspend fun create(create: CreateTask): DomainResult<Task>

    public suspend fun update(id: Id<Task>, update: UpdateTask): DomainResult<Task>

    public suspend fun delete(id: Id<Task>): DomainResult<Unit>
}
