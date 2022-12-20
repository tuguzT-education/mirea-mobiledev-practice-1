package io.github.tuguzt.mirea.todolist.data.interactor

import io.github.tuguzt.mirea.todolist.data.repository.TaskRepository
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.usecase.DeleteTask

public class DeleteTaskInteractor(private val repository: TaskRepository) : DeleteTask {
    override suspend fun deleteTask(task: Task): DomainResult<Unit> = repository.delete(task)
}
