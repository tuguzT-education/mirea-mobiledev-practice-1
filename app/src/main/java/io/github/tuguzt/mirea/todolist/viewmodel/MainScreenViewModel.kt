package io.github.tuguzt.mirea.todolist.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tuguzt.mirea.todolist.domain.Result
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.usecase.AllProjects
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val allProjects: AllProjects,
) : ViewModel() {
    private var _state by mutableStateOf(MainScreenState())
    val state get() = _state

    init {
        update()
    }

    fun update() {
        viewModelScope.launch {
            when (val result = allProjects.allProjects()) {
                is Result.Error -> throw result.error
                is Result.Success -> {
                    _state = state.copy(projects = result.data)
                }
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
