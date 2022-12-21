package io.github.tuguzt.mirea.todolist.data.interactor

import io.github.tuguzt.mirea.todolist.data.repository.TaskRepository
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.map
import io.github.tuguzt.mirea.todolist.domain.model.UpdateTask
import io.github.tuguzt.mirea.todolist.domain.usecase.CloseTask

public class CloseTaskInteractor(private val repository: TaskRepository) : CloseTask {
    override suspend fun closeTask(id: String): DomainResult<Unit> {
        val update = UpdateTask(completed = true)
        return repository.update(id, update).map { }
    }
}
