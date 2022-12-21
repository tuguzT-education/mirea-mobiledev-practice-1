package io.github.tuguzt.mirea.todolist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.github.tuguzt.mirea.todolist.data.datasource.ProjectDataSource
import io.github.tuguzt.mirea.todolist.data.datasource.TaskDataSource
import io.github.tuguzt.mirea.todolist.data.interactor.*
import io.github.tuguzt.mirea.todolist.data.repository.ProjectRepository
import io.github.tuguzt.mirea.todolist.data.repository.TaskRepository
import io.github.tuguzt.mirea.todolist.domain.usecase.*

@Module
@InstallIn(ActivityComponent::class)
object AppModule {
    @Provides
    fun provideProjectRepository(dataSource: ProjectDataSource): ProjectRepository =
        ProjectRepository(dataSource)

    @Provides
    fun provideTaskRepository(dataSource: TaskDataSource): TaskRepository =
        TaskRepository(dataSource)

    @Provides
    fun provideAllProjects(repository: ProjectRepository): AllProjects =
        AllProjectsInteractor(repository)

    @Provides
    fun provideAllTasks(repository: TaskRepository): AllTasks =
        AllTasksInteractor(repository)

    @Provides
    fun provideCloseTask(repository: TaskRepository): CloseTask =
        CloseTaskInteractor(repository)

    @Provides
    fun provideCreateProject(repository: ProjectRepository): CreateProject =
        CreateProjectInteractor(repository)

    @Provides
    fun provideCreateTask(repository: TaskRepository): CreateTask =
        CreateTaskInteractor(repository)

    @Provides
    fun provideDeleteProject(repository: ProjectRepository): DeleteProject =
        DeleteProjectInteractor(repository)

    @Provides
    fun provideDeleteTask(repository: TaskRepository): DeleteTask =
        DeleteTaskInteractor(repository)

    @Provides
    fun provideProjectById(repository: ProjectRepository): ProjectById =
        ProjectByIdInteractor(repository)

    @Provides
    fun provideReopenTask(repository: TaskRepository): ReopenTask =
        ReopenTaskInteractor(repository)

    @Provides
    fun provideTaskById(repository: TaskRepository): TaskById =
        TaskByIdInteractor(repository)
}
