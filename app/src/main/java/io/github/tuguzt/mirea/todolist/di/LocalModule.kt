package io.github.tuguzt.mirea.todolist.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.tuguzt.mirea.todolist.data.datasource.local.DatabaseClient
import io.github.tuguzt.mirea.todolist.data.datasource.local.LocalProjectDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.local.LocalTaskDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    @Singleton
    fun provideClient(@ApplicationContext context: Context): DatabaseClient =
        DatabaseClient(context)

    @Provides
    fun provideProjectDataSource(client: DatabaseClient): LocalProjectDataSource =
        LocalProjectDataSource(client)

    @Provides
    fun provideTaskDataSource(client: DatabaseClient): LocalTaskDataSource =
        LocalTaskDataSource(client)
}
