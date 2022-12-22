package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.map
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.model.UpdateTask
import io.github.tuguzt.mirea.todolist.domain.repository.TaskRepository

public class ReopenTask(private val repository: TaskRepository) {
    public suspend fun reopenTask(id: Id<Task>): DomainResult<Unit> {
        val update = UpdateTask(completed = false)
        return repository.update(id, update).map { }
    }
}
