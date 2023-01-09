package io.github.tuguzt.mirea.todolist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.tuguzt.mirea.todolist.data.datasource.local.LocalProjectDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.local.LocalTaskDataSource
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
    fun provideProjectRepository(
        remoteDataSource: RemoteProjectDataSource,
        localDataSource: LocalProjectDataSource,
    ): ProjectRepository = ProjectRepositoryImpl(remoteDataSource, localDataSource)

    @Provides
    fun provideTaskRepository(
        remoteDataSource: RemoteTaskDataSource,
        localDataSource: LocalTaskDataSource,
    ): TaskRepository = TaskRepositoryImpl(remoteDataSource, localDataSource)
}
