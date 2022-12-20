package io.github.tuguzt.mirea.todolist.data.interactor

import io.github.tuguzt.mirea.todolist.data.repository.TaskRepository
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.usecase.AllTasks

public class AllTasksInteractor(private val repository: TaskRepository) : AllTasks {
    override suspend fun allTasks(): DomainResult<List<Task>> = repository.getAll()
}
