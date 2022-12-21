package io.github.tuguzt.mirea.todolist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.github.tuguzt.mirea.todolist.data.datasource.ProjectDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.TaskDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.remote.ApiClient
import io.github.tuguzt.mirea.todolist.data.datasource.remote.RemoteProjectDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.remote.RemoteTaskDataSource

@Module
@InstallIn(ActivityComponent::class)
object RemoteModule {
    @Provides
    fun provideApiClient(): ApiClient = ApiClient()

    @Provides
    fun provideProjectDataSource(apiClient: ApiClient): ProjectDataSource =
        RemoteProjectDataSource(apiClient)

    @Provides
    fun provideTaskDataSource(apiClient: ApiClient): TaskDataSource =
        RemoteTaskDataSource(apiClient)
}
