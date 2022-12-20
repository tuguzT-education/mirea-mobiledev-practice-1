package io.github.tuguzt.mirea.todolist.data.interactor

import io.github.tuguzt.mirea.todolist.data.repository.TaskRepository
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.usecase.CreateTask

public class CreateTaskInteractor(private val repository: TaskRepository) : CreateTask {
    override suspend fun createTask(
        parent: Project,
        name: String,
        content: String
    ): DomainResult<Task> = repository.create(parent, name, content, completed = false, due = null)
}
