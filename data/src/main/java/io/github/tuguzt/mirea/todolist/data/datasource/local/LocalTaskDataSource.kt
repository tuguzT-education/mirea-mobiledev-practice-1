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
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

public class LocalTaskDataSource(client: DatabaseClient) : TaskDataSource {
    private val projectBox = client.projectBox
    private val taskBox = client.taskBox

    override suspend fun getAll(project: Id<Project>): DomainResult<List<Task>> =
        withContext(Dispatchers.IO) {
            val data = taskBox.all
                .filter { task -> task.project.target?.uid == project.value }
                .map(TaskEntity::toDomain)
            success(data)
        }

    override suspend fun findById(id: Id<Task>): DomainResult<Task?> =
        withContext(Dispatchers.IO) {
            val query = taskBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(TaskEntity_.uid, id.value, stringOrder)
            }
            val entity = query.findUnique()
            success(entity?.toDomain())
        }

    override suspend fun create(create: CreateTask): DomainResult<Task> =
        withContext(Dispatchers.IO) {
            val projectQuery = projectBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(ProjectEntity_.uid, create.project.value, stringOrder)
            }
            val project = projectQuery.findUnique() ?: kotlin.run {
                val error = DomainError.StorageError(
                    message = "Project was not found by id ${create.project}",
                    cause = null,
                )
                return@withContext error(error)
            }
            var entity = TaskEntity(
                name = create.name,
                content = create.content,
                createdAt = Clock.System.now().toEpochMilliseconds(),
            )
            entity.project.target = project
            val id = taskBox.put(entity)

            entity = taskBox[id]
            entity.uid = id.toString()
            taskBox.put(entity)
            success(entity.toDomain())
        }

    public suspend fun save(project: Id<Project>, task: Task): DomainResult<Task> =
        withContext(Dispatchers.IO) {
            val projectQuery = projectBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(ProjectEntity_.uid, project.value, stringOrder)
            }
            val projectEntity = projectQuery.findUnique() ?: kotlin.run {
                val error = DomainError.StorageError(
                    message = "Project was not found by id $project",
                    cause = null,
                )
                return@withContext error(error)
            }
            val query = taskBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(TaskEntity_.uid, task.id.value, stringOrder)
            }
            val entity = query.findUnique() ?: TaskEntity(
                uid = task.id.value,
                name = task.name,
                content = task.content,
                completed = task.completed,
                createdAt = task.createdAt.toEpochMilliseconds(),
            )
            entity.project.target = projectEntity
            entity.due.target = task.due?.let { due ->
                TaskDueEntity(string = due.string, datetime = due.datetime.toEpochMilliseconds())
            }
            taskBox.put(entity)
            success(entity.toDomain())
        }

    override suspend fun update(id: Id<Task>, update: UpdateTask): DomainResult<Task> =
        withContext(Dispatchers.IO) {
            val query = taskBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(TaskEntity_.uid, id.value, stringOrder)
            }
            val entity = query.findUnique() ?: kotlin.run {
                val error = DomainError.StorageError(
                    message = "Task was not found by id $id",
                    cause = null,
                )
                return@withContext error(error)
            }
            update.name?.let { name ->
                entity.name = name
            }
            update.content?.let { content ->
                entity.content = content
            }
            update.completed?.let { completed ->
                entity.completed = completed
            }
            update.due?.let { due ->
                if (due.hasUpdates()) {
                    val dueEntity = entity.due.target ?: TaskDueEntity()
                    due.string?.let { string ->
                        dueEntity.string = string
                    }
                    due.datetime?.let { datetime ->
                        dueEntity.datetime = datetime.toEpochMilliseconds()
                    }
                    entity.due.target = dueEntity
                }
            }
            taskBox.put(entity)
            success(entity.toDomain())
        }

    override suspend fun delete(id: Id<Task>): DomainResult<Unit> =
        withContext(Dispatchers.IO) {
            val query = taskBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(TaskEntity_.uid, id.value, stringOrder)
            }
            val entity = query.findUnique() ?: kotlin.run {
                val error = DomainError.StorageError(
                    message = "Task was not found by id $id",
                    cause = null,
                )
                return@withContext error(error)
            }
            taskBox.remove(entity)
            success(Unit)
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
