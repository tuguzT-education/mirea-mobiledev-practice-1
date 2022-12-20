package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.model.Project

public interface DeleteProject {
    public suspend fun deleteProject(project: Project)
}
