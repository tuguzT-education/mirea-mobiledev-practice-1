package io.github.tuguzt.mirea.todolist.viewmodel.project

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.viewmodel.fakeProjects
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddNewProjectViewModel @Inject constructor() : ViewModel() {
    private var _state by mutableStateOf(AddNewProjectScreenState())
    val state get() = _state

    var projectName: String
        get() = state.name
        set(name) {
            _state = state.copy(name = name)
        }

    fun addNewProject() {
        val newProject = Project(
            id = UUID.randomUUID().toString(),
            name = state.name,
            tasks = listOf(),
        )
        fakeProjects = fakeProjects + newProject
    }
}

@Immutable
data class AddNewProjectScreenState(val name: String = "")
