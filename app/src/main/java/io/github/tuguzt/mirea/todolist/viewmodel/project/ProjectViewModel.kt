package io.github.tuguzt.mirea.todolist.viewmodel.project

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.viewmodel.fakeProjects
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor() : ViewModel() {
    private var _state: ProjectScreenState? by mutableStateOf(null)
    val state get() = checkNotNull(_state)

    fun setup(projectId: String) {
        val project = fakeProjects.find { project -> project.id == projectId }
            ?: error("Project with id $projectId not present")
        _state = ProjectScreenState(project)
    }
}

@Immutable
data class ProjectScreenState(
    val project: Project,
)
