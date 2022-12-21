package io.github.tuguzt.mirea.todolist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.tuguzt.mirea.todolist.data.datasource.remote.ApiClient
import io.github.tuguzt.mirea.todolist.data.datasource.remote.RemoteProjectDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.remote.RemoteTaskDataSource

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Provides
    fun provideClient(): ApiClient = ApiClient()

    @Provides
    fun provideProjectDataSource(apiClient: ApiClient): RemoteProjectDataSource =
        RemoteProjectDataSource(apiClient)

    @Provides
    fun provideTaskDataSource(apiClient: ApiClient): RemoteTaskDataSource =
        RemoteTaskDataSource(apiClient)
}
