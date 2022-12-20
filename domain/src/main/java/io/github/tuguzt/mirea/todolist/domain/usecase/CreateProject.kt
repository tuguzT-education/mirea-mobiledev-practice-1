package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.model.Project

public interface CreateProject {
    public suspend fun createProject(name: String): Project
}
