package io.github.tuguzt.mirea.todolist.data.datasource.local

import io.github.tuguzt.mirea.todolist.data.datasource.ProjectDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.ProjectEntity
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.ProjectEntity_
import io.github.tuguzt.mirea.todolist.data.datasource.local.model.TaskEntity
import io.github.tuguzt.mirea.todolist.domain.DomainError
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.error
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.UpdateProject
import io.github.tuguzt.mirea.todolist.domain.success
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public class LocalProjectDataSource(client: DatabaseClient) : ProjectDataSource {
    private val projectBox = client.projectBox

    override suspend fun getAll(): DomainResult<List<Project>> =
        withContext(Dispatchers.IO) {
            success(projectBox.all.map(ProjectEntity::toDomain))
        }

    override suspend fun findById(id: String): DomainResult<Project?> =
        withContext(Dispatchers.IO) {
            val query = projectBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(ProjectEntity_.uid, id, stringOrder)
            }
            val entity = query.findUnique()
            success(entity?.toDomain())
        }

    override suspend fun create(name: String): DomainResult<Project> {
        TODO("Not yet implemented")
    }

    public suspend fun save(project: Project): DomainResult<Project> =
        withContext(Dispatchers.IO) {
            val query = projectBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(ProjectEntity_.uid, project.id, stringOrder)
            }
            val entity = query.findUnique() ?: ProjectEntity(uid = project.id, name = project.name)
            projectBox.put(entity)
            success(entity.toDomain())
        }

    override suspend fun update(id: String, update: UpdateProject): DomainResult<Project> =
        withContext(Dispatchers.IO) {
            val query = projectBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(ProjectEntity_.uid, id, stringOrder)
            }
            val entity = query.findUnique() ?: kotlin.run {
                val error = DomainError.StorageError(
                    message = "Project was not found by id $id",
                    cause = null,
                )
                return@withContext error(error)
            }
            entity.name = update.name
            projectBox.put(entity)
            success(entity.toDomain())
        }

    override suspend fun delete(id: String): DomainResult<Unit> =
        withContext(Dispatchers.IO) {
            val query = projectBox.query {
                val stringOrder = QueryBuilder.StringOrder.CASE_SENSITIVE
                equal(ProjectEntity_.uid, id, stringOrder)
            }
            val entity = query.findUnique() ?: kotlin.run {
                val error = DomainError.StorageError(
                    message = "Project was not found by id $id",
                    cause = null,
                )
                return@withContext error(error)
            }
            projectBox.remove(entity)
            success(Unit)
        }
}

internal fun ProjectEntity.toDomain(): Project = Project(
    id = requireNotNull(uid),
    name = requireNotNull(name),
    tasks = tasks.map(TaskEntity::toDomain),
)