package io.github.tuguzt.mirea.todolist.viewmodel.project

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tuguzt.mirea.todolist.domain.Result
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.usecase.ProjectById
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectById: ProjectById,
) : ViewModel() {
    private var _state by mutableStateOf(ProjectScreenState())
    val state get() = _state

    fun setup(projectId: String) {
        viewModelScope.launch {
            when (val result = projectById.projectById(projectId)) {
                is Result.Error -> throw result.error
                is Result.Success -> {
                    val project = checkNotNull(result.data)
                    _state = state.copy(project = project, loading = false)
                }
            }
        }
    }
}

@Immutable
data class ProjectScreenState(
    val project: Project? = null,
    val loading: Boolean = true,
)
