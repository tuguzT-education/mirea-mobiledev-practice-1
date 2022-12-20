package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.model.Project

public interface ProjectById {
    public suspend fun projectById(id: String): Project?
}
