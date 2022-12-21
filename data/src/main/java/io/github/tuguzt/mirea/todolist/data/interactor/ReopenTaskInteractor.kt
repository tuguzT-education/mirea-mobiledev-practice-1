package io.github.tuguzt.mirea.todolist.data.interactor

import io.github.tuguzt.mirea.todolist.data.repository.TaskRepository
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.map
import io.github.tuguzt.mirea.todolist.domain.model.UpdateTask
import io.github.tuguzt.mirea.todolist.domain.usecase.ReopenTask

public class ReopenTaskInteractor(private val repository: TaskRepository) : ReopenTask {
    override suspend fun reopenTask(id: String): DomainResult<Unit> {
        val update = UpdateTask(completed = false)
        return repository.update(id, update).map { }
    }
}
