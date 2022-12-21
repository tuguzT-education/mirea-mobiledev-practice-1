package io.github.tuguzt.mirea.todolist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.tuguzt.mirea.todolist.data.datasource.local.LocalProjectDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.local.LocalTaskDataSource
import io.github.tuguzt.mirea.todolist.data.repository.ProjectRepository
import io.github.tuguzt.mirea.todolist.data.repository.TaskRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideProjectRepository(dataSource: LocalProjectDataSource): ProjectRepository =
        ProjectRepository(dataSource)

    @Provides
    fun provideTaskRepository(dataSource: LocalTaskDataSource): TaskRepository =
        TaskRepository(dataSource)
}
