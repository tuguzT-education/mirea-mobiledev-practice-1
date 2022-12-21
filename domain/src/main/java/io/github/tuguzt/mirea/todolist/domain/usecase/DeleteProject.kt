package io.github.tuguzt.mirea.todolist.domain.usecase

import io.github.tuguzt.mirea.todolist.domain.DomainResult

public interface DeleteProject {
    public suspend fun deleteProject(id: String): DomainResult<Unit>
}
