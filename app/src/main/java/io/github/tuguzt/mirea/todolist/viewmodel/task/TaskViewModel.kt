package io.github.tuguzt.mirea.todolist.viewmodel.task

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.viewmodel.fakeProjects
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor() : ViewModel() {
    private var _state: TaskScreenState? by mutableStateOf(null)
    val state get() = checkNotNull(_state)

    fun setup(projectId: String, taskId: String) {
        val project = fakeProjects.find { project -> project.id == projectId }
            ?: error("Project with id $projectId not present")
        val task = project.tasks.find { task -> task.id == taskId }
            ?: error("Task with id $taskId not present")
        _state = TaskScreenState(task)
    }
}

@Immutable
data class TaskScreenState(
    val task: Task,
)
