package io.github.tuguzt.mirea.todolist.data.datasource

import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.model.TaskDue

public interface TaskDataSource {
    public suspend fun getAll(): List<Task>

    public suspend fun findById(id: String): Task?

    public suspend fun create(
        parent: Project,
        name: String,
        content: String,
        completed: Boolean,
        due: TaskDue?,
    ): Task

    public suspend fun update(task: Task): Task

    public suspend fun delete(task: Task)
}
