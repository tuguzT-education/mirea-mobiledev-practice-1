package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.CreateTask
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.repository.TaskRepository

public class CreateTask(private val repository: TaskRepository) {
    public suspend fun createTask(create: CreateTask): DomainResult<Task> =
        repository.create(create)
}
