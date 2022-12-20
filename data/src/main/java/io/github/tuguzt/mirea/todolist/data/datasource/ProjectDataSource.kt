package io.github.tuguzt.mirea.todolist.data.datasource

import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task

public interface ProjectDataSource {
    public suspend fun getAll(): List<Project>

    public suspend fun findById(id: String): Project?

    public suspend fun create(name: String, tasks: List<Task>): Project

    public suspend fun update(project: Project): Project

    public suspend fun delete(project: Project)
}
