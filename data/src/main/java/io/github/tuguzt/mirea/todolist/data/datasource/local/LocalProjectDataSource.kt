package io.github.tuguzt.mirea.todolist.data.datasource.local

import io.github.tuguzt.mirea.todolist.data.datasource.local.model.ProjectEntity
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.ProjectEntity_
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.TaskEntity
import io.github.tuguzt.mirea.todolist.domain.DomainError
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.error
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.success
import io.objectbox.exception.DbException
import io.objectbox.kotlin.awaitCallInTx
import io.objectbox.kotlin.flow
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class LocalProjectDataSource(private val client: DatabaseClient) {
    @OptIn(ExperimentalCoroutinesApi::class)
    public fun getAll(): DomainResult<Flow<List<Project>>> = try {
        val query = client.projectBox.query {}
        val flow = query.flow().map { entities -> entities.map(ProjectEntity::toDomain) }
        success(flow)
    } catch (dbException: DbException) {
        val error = DomainError.StorageError(
            message = "Failed to get all projects",
            cause = dbException,
        )
        error(error)
    } catch (exception: Exception) {
        val error = DomainError.LogicError(
            message = "Failed to get all projects",
            cause = exception,
        )
        error(error)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    public fun findById(id: Id<Project>): DomainResult<Flow<Project?>> = try {
        val query = client.projectBox.query {
            val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
            equal(ProjectEntity_.uid, id.value, stringOrder)
        }
        val flow = query.flow().map { entities -> entities.firstOrNull()?.toDomain() }
        success(flow)
    } catch (dbException: DbException) {
        val error = DomainError.StorageError(
            message = "Failed to get project by id '$id'",
            cause = dbException,
        )
        error(error)
    } catch (exception: Exception) {
        val error = DomainError.LogicError(
            message = "Failed to get project by id '$id'",
            cause = exception,
        )
        error(error)
    }

    public suspend fun save(project: Project): DomainResult<Project> = try {
        val entity = client.boxStore.awaitCallInTx {
            val query = client.projectBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(ProjectEntity_.uid, project.id.value, stringOrder)
            }
            val entity = query.findUnique()?.apply { name = project.name }
                ?: ProjectEntity(uid = project.id.value, name = project.name)
            client.projectBox.put(entity)
            entity
        }!!
        success(entity.toDomain())
    } catch (dbException: DbException) {
        val error = DomainError.StorageError(
            message = "Failed to save project $project",
            cause = dbException,
        )
        error(error)
    } catch (exception: Exception) {
        val error = DomainError.LogicError(
            message = "Failed to save project $project",
            cause = exception,
        )
        error(error)
    }

    public suspend fun saveAll(projects: List<Project>): DomainResult<List<Project>> = try {
        val entities = client.boxStore.awaitCallInTx {
            val entities = projects.map { project ->
                val query = client.projectBox.query {
                    val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                    equal(ProjectEntity_.uid, project.id.value, stringOrder)
                }
                query.findUnique()?.apply { name = project.name }
                    ?: ProjectEntity(uid = project.id.value, name = project.name)
            }
            client.projectBox.put(entities)
            entities
        }!!
        success(entities.map(ProjectEntity::toDomain))
    } catch (dbException: DbException) {
        val error = DomainError.StorageError(
            message = "Failed to save projects $projects",
            cause = dbException,
        )
        error(error)
    } catch (exception: Exception) {
        val error = DomainError.LogicError(
            message = "Failed to save projects $projects",
            cause = exception,
        )
        error(error)
    }

    public suspend fun delete(id: Id<Project>): DomainResult<Unit> = try {
        client.boxStore.awaitCallInTx {
            val query = client.projectBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(ProjectEntity_.uid, id.value, stringOrder)
            }
            val entity = query.findUnique()
                ?: throw IllegalStateException("Project was not found by id '$id'")
            client.projectBox.remove(entity)
        }
        success(Unit)
    } catch (dbException: DbException) {
        val error = DomainError.StorageError(
            message = "Failed to delete project by id '$id'",
            cause = dbException,
        )
        error(error)
    } catch (exception: Exception) {
        val error = DomainError.LogicError(
            message = "Failed to delete project by id '$id'",
            cause = exception,
        )
        error(error)
    }
}

internal fun ProjectEntity.toDomain(): Project = Project(
    id = Id(value = requireNotNull(uid)),
    name = requireNotNull(name),
    tasks = tasks.map(TaskEntity::toDomain),
)
