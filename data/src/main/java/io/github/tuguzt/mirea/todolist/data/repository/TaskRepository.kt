package io.github.tuguzt.mirea.todolist.data.repository

import io.github.tuguzt.mirea.todolist.data.datasource.TaskDataSource
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.model.TaskDue

public class TaskRepository(private val dataSource: TaskDataSource) {
    public suspend fun getAll(): List<Task> = dataSource.getAll()

    public suspend fun findById(id: String): Task? = dataSource.findById(id)

    public suspend fun create(
        parent: Project,
        name: String,
        content: String,
        completed: Boolean,
        due: TaskDue?,
    ): Task = dataSource.create(parent, name, content, completed, due)

    public suspend fun update(task: Task): Task = dataSource.update(task)

    public suspend fun delete(task: Task): Unit = dataSource.delete(task)
}
