package io.github.tuguzt.mirea.todolist.data.datasource

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.CreateProject
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.UpdateProject

public interface ProjectDataSource {
    public suspend fun getAll(): DomainResult<List<Project>>

    public suspend fun findById(id: Id<Project>): DomainResult<Project?>

    public suspend fun create(create: CreateProject): DomainResult<Project>

    public suspend fun update(id: Id<Project>, update: UpdateProject): DomainResult<Project>

    public suspend fun delete(id: Id<Project>): DomainResult<Unit>
}
