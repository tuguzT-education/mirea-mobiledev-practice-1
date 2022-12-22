package io.github.tuguzt.mirea.todolist.data.datasource.local

import io.github.tuguzt.mirea.todolist.data.datasource.ProjectDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.ProjectEntity
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.ProjectEntity_
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.TaskEntity
import io.github.tuguzt.mirea.todolist.domain.DomainError
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.error
import io.github.tuguzt.mirea.todolist.domain.model.CreateProject
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.UpdateProject
import io.github.tuguzt.mirea.todolist.domain.success
import io.objectbox.kotlin.awaitCallInTx
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder

public class LocalProjectDataSource(private val client: DatabaseClient) : ProjectDataSource {
    override suspend fun getAll(): DomainResult<List<Project>> {
        val all = client.boxStore.awaitCallInTx { client.projectBox.all }!!
            .map(ProjectEntity::toDomain)
        return success(all)
    }

    override suspend fun findById(id: Id<Project>): DomainResult<Project?> {
        val entity = client.boxStore.awaitCallInTx {
            val query = client.projectBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(ProjectEntity_.uid, id.value, stringOrder)
            }
            query.findUnique()
        }
        return success(entity?.toDomain())
    }

    override suspend fun create(create: CreateProject): DomainResult<Project> {
        val entity = client.boxStore.awaitCallInTx {
            val entity = ProjectEntity(name = create.name)
            val id = client.projectBox.put(entity)
            entity.uid = id.toString()
            client.projectBox.put(entity)
            entity
        }!!
        return success(entity.toDomain())
    }

    public suspend fun save(project: Project): DomainResult<Project> {
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
        return success(entity.toDomain())
    }

    override suspend fun update(id: Id<Project>, update: UpdateProject): DomainResult<Project> {
        val entity = client.boxStore.awaitCallInTx {
            val query = client.projectBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(ProjectEntity_.uid, id.value, stringOrder)
            }
            val entity = query.findUnique()
            entity?.let {
                it.name = update.name
                client.projectBox.put(it)
                it
            }
        } ?: kotlin.run {
            val error = DomainError.StorageError(
                message = "Project was not found by id $id",
                cause = null,
            )
            return error(error)
        }
        return success(entity.toDomain())
    }

    override suspend fun delete(id: Id<Project>): DomainResult<Unit> {
        client.boxStore.awaitCallInTx {
            val query = client.projectBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(ProjectEntity_.uid, id.value, stringOrder)
            }
            val entity = query.findUnique()
            entity?.let {
                client.projectBox.remove(it)
                it
            }
        } ?: kotlin.run {
            val error = DomainError.StorageError(
                message = "Project was not found by id $id",
                cause = null,
            )
            return error(error)
        }
        return success(Unit)
    }
}

internal fun ProjectEntity.toDomain(): Project = Project(
    id = Id(value = requireNotNull(uid)),
    name = requireNotNull(name),
    tasks = tasks.map(TaskEntity::toDomain),
)
