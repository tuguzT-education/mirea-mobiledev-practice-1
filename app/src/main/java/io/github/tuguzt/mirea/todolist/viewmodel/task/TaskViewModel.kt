package io.github.tuguzt.mirea.todolist.viewmodel.task

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tuguzt.mirea.todolist.domain.Result
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.usecase.TaskById
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskById: TaskById,
) : ViewModel() {
    private var _state by mutableStateOf(TaskScreenState())
    val state get() = _state

    fun setup(taskId: String) {
        viewModelScope.launch {
            when (val result = taskById.taskById(taskId)) {
                is Result.Error -> throw result.error
                is Result.Success -> {
                    val task = checkNotNull(result.data)
                    _state = state.copy(task = task, loading = false)
                }
            }
        }
    }
}

@Immutable
data class TaskScreenState(
    val task: Task? = null,
    val loading: Boolean = true,
)
