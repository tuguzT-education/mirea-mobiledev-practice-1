package io.github.tuguzt.mirea.todolist.data.interactor

import io.github.tuguzt.mirea.todolist.data.repository.TaskRepository
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.map
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.usecase.CloseTask

public class CloseTaskInteractor(private val repository: TaskRepository) : CloseTask {
    override suspend fun closeTask(task: Task): DomainResult<Unit> {
        val updatedTask = task.copy(completed = true)
        return repository.update(updatedTask).map { }
    }
}
