package io.github.tuguzt.mirea.todolist.data.datasource

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task

public interface TaskDataSource {
    public suspend fun getAll(parent: Project): DomainResult<List<Task>>

    public suspend fun findById(id: String): DomainResult<Task?>

    public suspend fun create(parent: Project, name: String, content: String): DomainResult<Task>

    public suspend fun update(task: Task): DomainResult<Task>

    public suspend fun delete(task: Task): DomainResult<Unit>
}
