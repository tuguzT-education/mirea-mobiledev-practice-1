package io.github.tuguzt.mirea.todolist.data.datasource

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task

public interface ProjectDataSource {
    public suspend fun getAll(): DomainResult<List<Project>>

    public suspend fun findById(id: String): DomainResult<Project?>

    public suspend fun create(name: String, tasks: List<Task>): DomainResult<Project>

    public suspend fun update(project: Project): DomainResult<Project>

    public suspend fun delete(project: Project): DomainResult<Unit>
}
