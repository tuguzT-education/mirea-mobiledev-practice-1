package io.github.tuguzt.mirea.todolist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.tuguzt.mirea.todolist.data.datasource.remote.RemoteProjectDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.remote.RemoteTaskDataSource
import io.github.tuguzt.mirea.todolist.data.repository.ProjectRepositoryImpl
import io.github.tuguzt.mirea.todolist.data.repository.TaskRepositoryImpl
import io.github.tuguzt.mirea.todolist.domain.repository.ProjectRepository
import io.github.tuguzt.mirea.todolist.domain.repository.TaskRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideProjectRepository(dataSource: RemoteProjectDataSource): ProjectRepository =
        ProjectRepositoryImpl(dataSource)

    @Provides
    fun provideTaskRepository(dataSource: RemoteTaskDataSource): TaskRepository =
        TaskRepositoryImpl(dataSource)
}
