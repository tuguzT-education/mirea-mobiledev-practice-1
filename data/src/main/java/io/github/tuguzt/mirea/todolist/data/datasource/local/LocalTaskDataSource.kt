package io.github.tuguzt.mirea.todolist.data.datasource.local

import io.github.tuguzt.mirea.todolist.data.datasource.TaskDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.ProjectEntity_
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.TaskDueEntity
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.TaskEntity
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.TaskEntity_
import io.github.tuguzt.mirea.todolist.domain.DomainError
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.error
import io.github.tuguzt.mirea.todolist.domain.model.*
import io.github.tuguzt.mirea.todolist.domain.success
import io.objectbox.kotlin.awaitCallInTx
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

public class LocalTaskDataSource(private val client: DatabaseClient) : TaskDataSource {
    override suspend fun getAll(project: Id<Project>): DomainResult<List<Task>> {
        val all = client.boxStore.awaitCallInTx { client.taskBox.all }!!
            .filter { task -> task.project.target?.uid == project.value }
            .map(TaskEntity::toDomain)
        return success(all)
    }

    override suspend fun findById(id: Id<Task>): DomainResult<Task?> {
        val entity = client.boxStore.awaitCallInTx {
            val query = client.taskBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(TaskEntity_.uid, id.value, stringOrder)
            }
            query.findUnique()
        }
        return success(entity?.toDomain())
    }

    override suspend fun create(create: CreateTask): DomainResult<Task> {
        val entity = client.boxStore.awaitCallInTx {
            val projectQuery = client.projectBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(ProjectEntity_.uid, create.project.value, stringOrder)
            }
            val project = projectQuery.findUnique()
            project?.let {
                val entity = TaskEntity(
                    name = create.name,
                    content = create.content,
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                )
                entity.project.target = it
                val id = client.taskBox.put(entity)
                entity.uid = id.toString()
                client.taskBox.put(entity)
                entity
            }
        } ?: kotlin.run {
            val error = DomainError.StorageError(
                message = "Project was not found by id ${create.project}",
                cause = null,
            )
            return error(error)
        }
        return success(entity.toDomain())
    }

    public suspend fun save(project: Id<Project>, task: Task): DomainResult<Task> {
        val entity = client.boxStore.awaitCallInTx {
            val projectQuery = client.projectBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(ProjectEntity_.uid, project.value, stringOrder)
            }
            val projectEntity = projectQuery.findUnique()
            projectEntity?.let {
                val query = client.taskBox.query {
                    val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                    equal(TaskEntity_.uid, task.id.value, stringOrder)
                }
                val entity = query.findUnique()?.apply {
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
                entity.project.target = projectEntity
                entity.due.target = task.due?.let { due ->
                    TaskDueEntity(
                        string = due.string,
                        datetime = due.datetime.toEpochMilliseconds(),
                    )
                }
                client.taskBox.put(entity)
                entity
            }
        } ?: kotlin.run {
            val error = DomainError.StorageError(
                message = "Project was not found by id $project",
                cause = null,
            )
            return error(error)
        }
        return success(entity.toDomain())
    }

    override suspend fun update(id: Id<Task>, update: UpdateTask): DomainResult<Task> {
        val entity = client.boxStore.awaitCallInTx {
            val query = client.taskBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(TaskEntity_.uid, id.value, stringOrder)
            }
            val entity = query.findUnique()
            entity?.let {
                update.name?.let { name ->
                    it.name = name
                }
                update.content?.let { content ->
                    it.content = content
                }
                update.completed?.let { completed ->
                    it.completed = completed
                }
                update.due?.let { due ->
                    if (due.hasUpdates()) {
                        val dueEntity = it.due.target ?: TaskDueEntity()
                        due.string?.let { string ->
                            dueEntity.string = string
                        }
                        due.datetime?.let { datetime ->
                            dueEntity.datetime = datetime.toEpochMilliseconds()
                        }
                        it.due.target = dueEntity
                    }
                }
                client.taskBox.put(it)
                it
            }
        } ?: kotlin.run {
            val error = DomainError.StorageError(
                message = "Task was not found by id $id",
                cause = null,
            )
            return error(error)
        }
        return success(entity.toDomain())
    }

    override suspend fun delete(id: Id<Task>): DomainResult<Unit> {
        client.boxStore.awaitCallInTx {
            val query = client.taskBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(TaskEntity_.uid, id.value, stringOrder)
            }
            val entity = query.findUnique()
            entity?.let {
                client.taskBox.remove(it)
                it
            }
        } ?: kotlin.run {
            val error = DomainError.StorageError(
                message = "Task was not found by id $id",
                cause = null,
            )
            return error(error)
        }
        return success(Unit)
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
