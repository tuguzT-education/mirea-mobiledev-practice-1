package io.github.tuguzt.mirea.todolist.data.datasource.local

import io.github.tuguzt.mirea.todolist.data.datasource.local.model.ProjectEntity_
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.TaskDueEntity
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.TaskEntity
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.TaskEntity_
import io.github.tuguzt.mirea.todolist.domain.DomainError
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.error
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.model.TaskDue
import io.github.tuguzt.mirea.todolist.domain.success
import io.objectbox.exception.DbException
import io.objectbox.kotlin.awaitCallInTx
import io.objectbox.kotlin.flow
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

public class LocalTaskDataSource(private val client: DatabaseClient) {
    @OptIn(ExperimentalCoroutinesApi::class)
    public fun getAll(project: Id<Project>): DomainResult<Flow<List<Task>>> = try {
        val query = client.taskBox.query {}
        val flow = query.flow().map { entities ->
            entities.filter { task -> task.project.target?.uid == project.value }
                .map(TaskEntity::toDomain)
        }
        success(flow)
    } catch (dbException: DbException) {
        val error = DomainError.StorageError(
            message = "Failed to get all tasks",
            cause = dbException,
        )
        error(error)
    } catch (exception: Exception) {
        val error = DomainError.LogicError(
            message = "Failed to get all tasks",
            cause = exception,
        )
        error(error)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    public fun findById(id: Id<Task>): DomainResult<Flow<Task?>> = try {
        val query = client.taskBox.query {
            val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
            equal(TaskEntity_.uid, id.value, stringOrder)
        }
        val flow = query.flow().map { entities -> entities.firstOrNull()?.toDomain() }
        success(flow)
    } catch (dbException: DbException) {
        val error = DomainError.StorageError(
            message = "Failed to get task by id '$id'",
            cause = dbException,
        )
        error(error)
    } catch (exception: Exception) {
        val error = DomainError.LogicError(
            message = "Failed to get task by id '$id'",
            cause = exception,
        )
        error(error)
    }

    public suspend fun save(project: Id<Project>?, task: Task): DomainResult<Task> = try {
        val entity = client.boxStore.awaitCallInTx {
            val projectEntity = project?.let {
                val query = client.projectBox.query {
                    val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                    equal(ProjectEntity_.uid, project.value, stringOrder)
                }
                query.findUnique()
                    ?: throw IllegalStateException("Project was not found by id '$project'")
            }
            val query = client.taskBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(TaskEntity_.uid, task.id.value, stringOrder)
            }
            val taskEntity = kotlin.run {
                val entity = query.findUnique()?.apply {
                    name = task.name
                    content = task.content
                    completed = task.completed
                    createdAt = task.createdAt.toEpochMilliseconds()
                }
                if (entity == null && projectEntity == null) {
                    throw IllegalStateException("Cannot create task $task without parent project")
                }
                entity ?: TaskEntity(
                    uid = task.id.value,
                    name = task.name,
                    content = task.content,
                    completed = task.completed,
                    createdAt = task.createdAt.toEpochMilliseconds(),
                )
            }
            projectEntity?.let { taskEntity.project.target = it }
            taskEntity.due.target = task.due?.let { due ->
                TaskDueEntity(
                    string = due.string,
                    datetime = due.datetime.toEpochMilliseconds(),
                )
            }
            client.taskBox.put(taskEntity)
            taskEntity
        }!!
        success(entity.toDomain())
    } catch (dbException: DbException) {
        val error = DomainError.StorageError(
            message = "Failed to save task $task",
            cause = dbException,
        )
        error(error)
    } catch (exception: Exception) {
        val error = DomainError.LogicError(
            message = "Failed to save task $task",
            cause = exception,
        )
        error(error)
    }

    public suspend fun saveAll(project: Id<Project>, tasks: List<Task>): DomainResult<List<Task>> =
        try {
            val entities = client.boxStore.awaitCallInTx {
                val projectQuery = client.projectBox.query {
                    val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                    equal(ProjectEntity_.uid, project.value, stringOrder)
                }
                val projectEntity = projectQuery.findUnique()
                    ?: throw IllegalStateException("Project was not found by id '$project'")
                val taskEntities = tasks.map { task ->
                    val query = client.taskBox.query {
                        val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                        equal(TaskEntity_.uid, task.id.value, stringOrder)
                    }
                    val taskEntity = query.findUnique()?.apply {
                        name = task.name
                        content = task.content
                        completed = task.completed
                        createdAt = task.createdAt.toEpochMilliseconds()
                    } ?: TaskEntity(
                        uid = task.id.value,
                        name = task.name,
                        content = task.content,
                        completed = task.completed,
                        createdAt = task.createdAt.toEpochMilliseconds(),
                    )
                    taskEntity.project.target = projectEntity
                    taskEntity.due.target = task.due?.let { due ->
                        TaskDueEntity(
                            string = due.string,
                            datetime = due.datetime.toEpochMilliseconds(),
                        )
                    }
                    taskEntity
                }
                client.taskBox.put(taskEntities)
                taskEntities
            }!!
            success(entities.map(TaskEntity::toDomain))
        } catch (dbException: DbException) {
            val error = DomainError.StorageError(
                message = "Failed to save tasks $tasks",
                cause = dbException,
            )
            error(error)
        } catch (exception: Exception) {
            val error = DomainError.LogicError(
                message = "Failed to save tasks $tasks",
                cause = exception,
            )
            error(error)
        }

    public suspend fun delete(id: Id<Task>): DomainResult<Unit> = try {
        client.boxStore.awaitCallInTx {
            val query = client.taskBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(TaskEntity_.uid, id.value, stringOrder)
            }
            val entity = query.findUnique()
                ?: throw IllegalStateException("Task was not found by id '$id'")
            client.taskBox.remove(entity)
        }
        success(Unit)
    } catch (dbException: DbException) {
        val error = DomainError.StorageError(
            message = "Failed to delete task by id '$id'",
            cause = dbException,
        )
        error(error)
    } catch (exception: Exception) {
        val error = DomainError.LogicError(
            message = "Failed to delete task by id '$id'",
            cause = exception,
        )
        error(error)
    }
}

internal fun TaskEntity.toDomain(): Task = Task(
    id = Id(value = requireNotNull(uid)),
    name = requireNotNull(name),
    content = requireNotNull(content),
    completed = completed,
    due = due.target?.toDomain(),
    createdAt = Instant.fromEpochMilliseconds(createdAt),
)

internal fun TaskDueEntity.toDomain(): TaskDue = TaskDue(
    string = requireNotNull(string),
    datetime = Instant.fromEpochMilliseconds(datetime),
)
