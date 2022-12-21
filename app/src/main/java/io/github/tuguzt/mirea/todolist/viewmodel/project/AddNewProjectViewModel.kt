package io.github.tuguzt.mirea.todolist.viewmodel.project

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tuguzt.mirea.todolist.domain.Result
import io.github.tuguzt.mirea.todolist.domain.usecase.CreateProject
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewProjectViewModel @Inject constructor(
    private val createProject: CreateProject,
) : ViewModel() {
    private var _state by mutableStateOf(AddNewProjectScreenState())
    val state get() = _state

    var projectName: String
        get() = state.name
        set(name) {
            _state = state.copy(name = name)
        }

    fun canAdd(): Boolean = projectName.isNotEmpty()

    fun addNewProject() {
        viewModelScope.launch {
            when (val result = createProject.createProject(state.name)) {
                is Result.Error -> throw result.error
                is Result.Success -> Unit
            }
        }
    }
}

@Immutable
data class AddNewProjectScreenState(val name: String = "")
