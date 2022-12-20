package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.model.Project

public interface AllProjects {
    public suspend fun allProjects(): List<Project>
}
