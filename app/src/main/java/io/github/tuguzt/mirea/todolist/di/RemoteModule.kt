package io.github.tuguzt.mirea.todolist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.tuguzt.mirea.todolist.data.datasource.remote.ApiClient
import io.github.tuguzt.mirea.todolist.data.datasource.remote.RemoteProjectDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.remote.RemoteTaskDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Provides
    @Singleton
    fun provideClient(): ApiClient = ApiClient()

    @Provides
    fun provideProjectDataSource(client: ApiClient): RemoteProjectDataSource =
        RemoteProjectDataSource(client)

    @Provides
    fun provideTaskDataSource(client: ApiClient): RemoteTaskDataSource =
        RemoteTaskDataSource(client)
}
