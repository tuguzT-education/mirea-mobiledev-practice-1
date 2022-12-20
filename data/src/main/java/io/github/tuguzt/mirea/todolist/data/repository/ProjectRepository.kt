package io.github.tuguzt.mirea.todolist.data.repository

import io.github.tuguzt.mirea.todolist.data.datasource.ProjectDataSource
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task

public class ProjectRepository(private val dataSource: ProjectDataSource) {
    public suspend fun getAll(): List<Project> = dataSource.getAll()

    public suspend fun findById(id: String): Project? = dataSource.findById(id)

    public suspend fun create(name: String, tasks: List<Task>): Project =
        dataSource.create(name, tasks)

    public suspend fun update(project: Project): Project = dataSource.update(project)

    public suspend fun delete(project: Project): Unit = dataSource.delete(project)
}
