package io.github.tuguzt.mirea.todolist.domain.repository

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.CreateProject
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.UpdateProject
import kotlinx.coroutines.flow.Flow

public interface ProjectRepository {
    public suspend fun getAll(): DomainResult<Flow<List<Project>>>

    public suspend fun refreshAll(): DomainResult<Unit>

    public suspend fun findById(id: Id<Project>): DomainResult<Flow<Project?>>

    public suspend fun refreshById(id: Id<Project>): DomainResult<Unit>

    public suspend fun create(create: CreateProject): DomainResult<Project>

    public suspend fun update(id: Id<Project>, update: UpdateProject): DomainResult<Project>

    public suspend fun delete(id: Id<Project>): DomainResult<Unit>
}
