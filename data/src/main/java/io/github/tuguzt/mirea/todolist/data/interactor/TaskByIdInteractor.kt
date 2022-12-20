package io.github.tuguzt.mirea.todolist.data.interactor

import io.github.tuguzt.mirea.todolist.data.repository.TaskRepository
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.usecase.TaskById

public class TaskByIdInteractor(private val repository: TaskRepository) : TaskById {
    override suspend fun taskById(id: String): Task? = repository.findById(id)
}
