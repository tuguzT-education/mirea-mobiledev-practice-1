package io.github.tuguzt.mirea.todolist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import kotlinx.datetime.Clock
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {
    companion object {
        private data class State(
            val projects: List<Project> = listOf(
                Project(
                    id = "42",
                    name = "Hello World",
                    tasks = listOf(),
                ),
                Project(
                    id = "43",
                    name = "My own project",
                    tasks = listOf(
                        Task(
                            id = "42",
                            name = "Buy milk",
                            content = "",
                            completed = true,
                            due = null,
                            createdAt = Clock.System.now(),
                        ),
                        Task(
                            id = "43",
                            name = "New task",
                            content = "## Some `markdown`",
                            completed = false,
                            due = null,
                            createdAt = Clock.System.now(),
                        ),
                    ),
                ),
                Project(
                    id = "44",
                    name = "Completed project",
                    tasks = listOf(
                        Task(
                            id = "42",
                            name = "Buy milk",
                            content = "",
                            completed = true,
                            due = null,
                            createdAt = Clock.System.now(),
                        ),
                        Task(
                            id = "43",
                            name = "New task",
                            content = "## Some `markdown`",
                            completed = true,
                            due = null,
                            createdAt = Clock.System.now(),
                        ),
                    ),
                ),
            ),
        )
    }

    private var _state by mutableStateOf(State())

    fun allProjects(): List<Project> = _state.projects

    fun todoProjects(): List<Project> = _state.projects
        .filter { project ->
            project.tasks.isEmpty() || project.tasks.size > project.tasks.count(Task::completed)
        }

    fun completedProjects(): List<Project> = _state.projects
        .filter { project ->
            project.tasks.isNotEmpty() && project.tasks.size == project.tasks.count(Task::completed)
        }
}
