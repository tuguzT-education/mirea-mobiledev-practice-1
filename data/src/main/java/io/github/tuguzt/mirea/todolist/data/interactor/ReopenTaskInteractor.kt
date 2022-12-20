package io.github.tuguzt.mirea.todolist.data.interactor

import io.github.tuguzt.mirea.todolist.data.repository.TaskRepository
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.map
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.usecase.ReopenTask

public class ReopenTaskInteractor(private val repository: TaskRepository) : ReopenTask {
    override suspend fun reopenTask(task: Task): DomainResult<Unit> {
        val updatedTask = task.copy(completed = false)
        return repository.update(updatedTask).map { }
    }
}
