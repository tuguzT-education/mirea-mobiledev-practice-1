package io.github.tuguzt.mirea.todolist.data.datasource.remote

import io.github.tuguzt.mirea.todolist.data.datasource.TaskDataSource
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.model.TaskDue

public class RemoteTaskDataSource(apiClient: ApiClient) : TaskDataSource {
    private val taskApi = apiClient.taskApi

    override suspend fun getAll(): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: String): Task? {
        TODO("Not yet implemented")
    }

    override suspend fun create(
        parent: Project,
        name: String,
        content: String,
        completed: Boolean,
        due: TaskDue?
    ): Task {
        TODO("Not yet implemented")
    }

    override suspend fun update(task: Task): Task {
        TODO("Not yet implemented")
    }

    override suspend fun delete(task: Task) {
        TODO("Not yet implemented")
    }
}
