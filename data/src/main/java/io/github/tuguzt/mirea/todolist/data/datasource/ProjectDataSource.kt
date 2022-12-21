package io.github.tuguzt.mirea.todolist.data.datasource

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.UpdateProject

public interface ProjectDataSource {
    public suspend fun getAll(): DomainResult<List<Project>>

    public suspend fun findById(id: String): DomainResult<Project?>

    public suspend fun create(name: String): DomainResult<Project>

    public suspend fun update(id: String, update: UpdateProject): DomainResult<Project>

    public suspend fun delete(id: String): DomainResult<Unit>
}
