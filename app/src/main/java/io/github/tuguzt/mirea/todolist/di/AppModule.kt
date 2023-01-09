package io.github.tuguzt.mirea.todolist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.tuguzt.mirea.todolist.domain.repository.ProjectRepository
import io.github.tuguzt.mirea.todolist.domain.repository.TaskRepository
import io.github.tuguzt.mirea.todolist.domain.usecase.*

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideAllProjects(repository: ProjectRepository): AllProjects =
        AllProjects(repository)

    @Provides
    fun provideAllTasks(repository: TaskRepository): AllTasks =
        AllTasks(repository)

    @Provides
    fun provideCloseTask(repository: TaskRepository): CloseTask =
        CloseTask(repository)

    @Provides
    fun provideCreateProject(repository: ProjectRepository): CreateProject =
        CreateProject(repository)

    @Provides
    fun provideCreateTask(repository: TaskRepository): CreateTask =
        CreateTask(repository)

    @Provides
    fun provideDeleteProject(repository: ProjectRepository): DeleteProject =
        DeleteProject(repository)

    @Provides
    fun provideDeleteTask(repository: TaskRepository): DeleteTask =
        DeleteTask(repository)

    @Provides
    fun provideProjectById(repository: ProjectRepository): ProjectById =
        ProjectById(repository)

    @Provides
    fun provideRefreshAllProjects(repository: ProjectRepository): RefreshAllProjects =
        RefreshAllProjects(repository)

    @Provides
    fun provideRefreshAllTasks(repository: TaskRepository): RefreshAllTasks =
        RefreshAllTasks(repository)

    @Provides
    fun provideRefreshProjectById(repository: ProjectRepository): RefreshProjectById =
        RefreshProjectById(repository)

    @Provides
    fun provideRefreshTaskById(repository: TaskRepository): RefreshTaskById =
        RefreshTaskById(repository)

    @Provides
    fun provideReopenTask(repository: TaskRepository): ReopenTask =
        ReopenTask(repository)

    @Provides
    fun provideTaskById(repository: TaskRepository): TaskById =
        TaskById(repository)
}
