package io.github.tuguzt.mirea.todolist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.tuguzt.mirea.todolist.data.datasource.local.LocalProjectDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.local.LocalTaskDataSource
import io.github.tuguzt.mirea.todolist.data.repository.ProjectRepositoryImpl
import io.github.tuguzt.mirea.todolist.data.repository.TaskRepositoryImpl
import io.github.tuguzt.mirea.todolist.domain.repository.ProjectRepository
import io.github.tuguzt.mirea.todolist.domain.repository.TaskRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideProjectRepository(dataSource: LocalProjectDataSource): ProjectRepository =
        ProjectRepositoryImpl(dataSource)

    @Provides
    fun provideTaskRepository(dataSource: LocalTaskDataSource): TaskRepository =
        TaskRepositoryImpl(dataSource)
}
