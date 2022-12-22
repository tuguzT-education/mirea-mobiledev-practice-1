package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.map
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.model.UpdateTask
import io.github.tuguzt.mirea.todolist.domain.repository.TaskRepository

public class CloseTask(private val repository: TaskRepository) {
    public suspend fun closeTask(id: Id<Task>): DomainResult<Unit> {
        val update = UpdateTask(completed = true)
        return repository.update(id, update).map { }
    }
}
