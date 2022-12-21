package io.github.tuguzt.mirea.todolist.data.datasource.remote

import io.github.tuguzt.mirea.todolist.data.datasource.TaskDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.remote.model.ApiCreateTask
import io.github.tuguzt.mirea.todolist.data.datasource.remote.model.ApiTask
import io.github.tuguzt.mirea.todolist.data.datasource.remote.model.ApiUpdateTask
import io.github.tuguzt.mirea.todolist.data.datasource.remote.model.toResult
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.Result
import io.github.tuguzt.mirea.todolist.domain.cast
import io.github.tuguzt.mirea.todolist.domain.map
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.model.TaskDue
import io.github.tuguzt.mirea.todolist.domain.model.UpdateTask
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

public class RemoteTaskDataSource(apiClient: ApiClient) : TaskDataSource {
    private val taskApi = apiClient.taskApi

    override suspend fun getAll(parent: Project): DomainResult<List<Task>> =
        taskApi.all(parent.id).toResult().map { tasks -> tasks.map(ApiTask::toDomain) }

    override suspend fun findById(id: String): DomainResult<Task?> =
        taskApi.find(id).toResult().map(ApiTask::toDomain)

    override suspend fun create(
        parent: Project,
        name: String,
        content: String,
    ): DomainResult<Task> {
        val create = ApiCreateTask(
            projectId = parent.id,
            content = name,
            description = content,
        )
        return taskApi.create(create).toResult().map(ApiTask::toDomain)
    }

    override suspend fun update(id: String, update: UpdateTask): DomainResult<Task> {
        val completed = update.completed
        if (completed != null) {
            if (completed) {
                when (val result = taskApi.close(id).toResult()) {
                    is Result.Error -> return result.cast()
                    is Result.Success -> Unit
                }
            } else {
                when (val result = taskApi.reopen(id).toResult()) {
                    is Result.Error -> return result.cast()
                    is Result.Success -> Unit
                }
            }
        }
        val apiUpdate = ApiUpdateTask(
            content = update.name,
            description = update.content,
            labels = null,
            priority = null,
            dueString = update.due?.string,
            dueDate = null,
            dueDatetime = update.due?.datetime?.toString(),
            dueLang = null,
            assigneeId = null,
        )
        return taskApi.update(id, apiUpdate).toResult().map(ApiTask::toDomain)
    }

    override suspend fun delete(id: String): DomainResult<Unit> =
        taskApi.delete(id).toResult()
}

internal fun ApiTask.toDomain(): Task = Task(
    id = id,
    name = content,
    content = description,
    completed = isCompleted,
    due = due?.let { due ->
        TaskDue(
            string = due.string,
            datetime = if (due.datetime != null) {
                Instant.parse(due.datetime)
            } else {
                due.date.atStartOfDayIn(TimeZone.currentSystemDefault())
            },
        )
    },
    createdAt = Instant.parse(createdAt),
)
