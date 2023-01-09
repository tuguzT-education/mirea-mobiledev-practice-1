package io.github.tuguzt.mirea.todolist.data.datasource.remote

import io.github.tuguzt.mirea.todolist.data.datasource.remote.model.ApiCreateTask
import io.github.tuguzt.mirea.todolist.data.datasource.remote.model.ApiTask
import io.github.tuguzt.mirea.todolist.data.datasource.remote.model.ApiUpdateTask
import io.github.tuguzt.mirea.todolist.data.datasource.remote.model.toResult
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.Result
import io.github.tuguzt.mirea.todolist.domain.cast
import io.github.tuguzt.mirea.todolist.domain.map
import io.github.tuguzt.mirea.todolist.domain.model.*
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

public class RemoteTaskDataSource(client: ApiClient) {
    private val taskApi = client.taskApi

    public suspend fun getAll(project: Id<Project>): DomainResult<List<Task>> =
        taskApi.all(project.value).toResult().map { tasks -> tasks.map(ApiTask::toDomain) }

    public suspend fun findById(id: Id<Task>): DomainResult<Task?> =
        taskApi.find(id.value).toResult().map(ApiTask::toDomain)

    public suspend fun create(create: CreateTask): DomainResult<Task> {
        val apiCreate = ApiCreateTask(
            projectId = create.project.value,
            content = create.name,
            description = create.content,
        )
        return taskApi.create(apiCreate).toResult().map(ApiTask::toDomain)
    }

    public suspend fun update(id: Id<Task>, update: UpdateTask): DomainResult<Task> {
        update.completed?.let { completed ->
            if (completed) {
                when (val result = taskApi.close(id.value).toResult()) {
                    is Result.Error -> return result.cast()
                    is Result.Success -> Unit
                }
            } else {
                when (val result = taskApi.reopen(id.value).toResult()) {
                    is Result.Error -> return result.cast()
                    is Result.Success -> Unit
                }
            }
        }
        if (update.copy(completed = null).hasUpdates()) {
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
            return taskApi.update(id.value, apiUpdate).toResult().map(ApiTask::toDomain)
        }
        return findById(id).map(::checkNotNull)
    }

    public suspend fun delete(id: Id<Task>): DomainResult<Unit> =
        taskApi.delete(id.value).toResult()
}

internal fun ApiTask.toDomain(): Task = Task(
    id = Id(id),
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
