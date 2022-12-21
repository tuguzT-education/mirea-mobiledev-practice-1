package io.github.tuguzt.mirea.todolist.data.repository

import io.github.tuguzt.mirea.todolist.data.datasource.TaskDataSource
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.model.UpdateTask

public class TaskRepository(private val dataSource: TaskDataSource) {
    public suspend fun getAll(parent: Project): DomainResult<List<Task>> =
        dataSource.getAll(parent)

    public suspend fun findById(id: String): DomainResult<Task?> =
        dataSource.findById(id)

    public suspend fun create(parent: Project, name: String, content: String): DomainResult<Task> =
        dataSource.create(parent, name, content)

    public suspend fun update(id: String, update: UpdateTask): DomainResult<Task> =
        dataSource.update(id, update)

    public suspend fun delete(id: String): DomainResult<Unit> =
        dataSource.delete(id)
}
