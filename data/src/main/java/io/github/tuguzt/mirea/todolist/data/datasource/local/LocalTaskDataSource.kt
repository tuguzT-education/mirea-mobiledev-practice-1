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

    override suspend fun getAll(parent: Project): DomainResult<List<Task>> =
        withContext(Dispatchers.IO) {
            val data = taskBox.all
                .filter { task -> parent.tasks.find { it.id == task.uid } != null }
                .map(TaskEntity::toDomain)
            success(data)
        }

    override suspend fun findById(id: String): DomainResult<Task?> =
        withContext(Dispatchers.IO) {
            val query = taskBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(TaskEntity_.uid, id, stringOrder)
            }
            val entity = query.findUnique()
            success(entity?.toDomain())
        }

    override suspend fun create(
        parent: Project,
        name: String,
        content: String,
    ): DomainResult<Task> =
        withContext(Dispatchers.IO) {
            val projectQuery = projectBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(ProjectEntity_.uid, parent.id, stringOrder)
            }
            val project = projectQuery.findUnique() ?: kotlin.run {
                val error = DomainError.StorageError(
                    message = "Project was not found by id ${parent.id}",
                    cause = null,
                )
                return@withContext error(error)
            }
            var entity = TaskEntity(
                name = name,
                content = content,
                createdAt = Clock.System.now().toEpochMilliseconds(),
            )
            entity.project.target = project
            val id = taskBox.put(entity)

            entity = taskBox[id]
            entity.uid = id.toString()
            taskBox.put(entity)
            success(entity.toDomain())
        }

    public suspend fun save(projectId: String, task: Task): DomainResult<Task> =
        withContext(Dispatchers.IO) {
            val projectQuery = projectBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(ProjectEntity_.uid, projectId, stringOrder)
            }
            val project = projectQuery.findUnique() ?: kotlin.run {
                val error = DomainError.StorageError(
                    message = "Project was not found by id $projectId",
                    cause = null,
                )
                return@withContext error(error)
            }
            val query = taskBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(TaskEntity_.uid, task.id, stringOrder)
            }
            val entity = query.findUnique() ?: TaskEntity(
                uid = task.id,
                name = task.name,
                content = task.content,
                completed = task.completed,
                createdAt = task.createdAt.toEpochMilliseconds(),
            )
            entity.project.target = project
            entity.due.target = task.due?.let { due ->
                TaskDueEntity(string = due.string, datetime = due.datetime.toEpochMilliseconds())
            }
            taskBox.put(entity)
            success(entity.toDomain())
        }

    override suspend fun update(id: String, update: UpdateTask): DomainResult<Task> =
        withContext(Dispatchers.IO) {
            val query = taskBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(TaskEntity_.uid, id, stringOrder)
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
                if (due.isNotEmpty()) {
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

    override suspend fun delete(id: String): DomainResult<Unit> =
        withContext(Dispatchers.IO) {
            val query = taskBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(TaskEntity_.uid, id, stringOrder)
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
    id = requireNotNull(uid),
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
