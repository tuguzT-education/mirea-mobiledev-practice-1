package io.github.tuguzt.mirea.todolist.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {
    private var _state by mutableStateOf(MainScreenState())
    val state get() = _state

    init {
        viewModelScope.launch {
            fakeProjects.collect { projects ->
                _state = state.copy(projects = projects)
            }
        }
    }
}

@Immutable
data class MainScreenState(
    val projects: List<Project> = listOf(),
)

val MainScreenState.todoProjects
    get() = projects.filter { project ->
        project.tasks.isEmpty() || project.tasks.size > project.tasks.count(Task::completed)
    }

val MainScreenState.completedProjects
    get() = projects.filter { project ->
        project.tasks.isNotEmpty() && project.tasks.size == project.tasks.count(Task::completed)
    }
