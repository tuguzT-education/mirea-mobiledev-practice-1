package io.github.tuguzt.mirea.todolist.data.datasource.remote

import io.github.tuguzt.mirea.todolist.data.datasource.TaskDataSource
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.model.TaskDue

public class RemoteTaskDataSource(apiClient: ApiClient) : TaskDataSource {
    private val taskApi = apiClient.taskApi

    override suspend fun getAll(): DomainResult<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: String): DomainResult<Task?> {
        TODO("Not yet implemented")
    }

    override suspend fun create(
        parent: Project,
        name: String,
        content: String,
        completed: Boolean,
        due: TaskDue?
    ): DomainResult<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun update(task: Task): DomainResult<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(task: Task): DomainResult<Unit> {
        TODO("Not yet implemented")
    }
}
