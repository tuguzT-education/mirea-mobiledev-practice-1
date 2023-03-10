package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

public class AllTasks(private val repository: TaskRepository) {
    public suspend fun allTasks(project: Id<Project>): DomainResult<Flow<List<Task>>> =
        repository.getAll(project)
}
