package io.github.tuguzt.mirea.todolist.data.interactor

import io.github.tuguzt.mirea.todolist.data.repository.TaskRepository
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.usecase.ReopenTask

public class ReopenTaskInteractor(private val repository: TaskRepository) : ReopenTask {
    override suspend fun reopenTask(task: Task) {
        val updatedTask = task.copy(completed = false)
        repository.update(updatedTask)
    }
}
