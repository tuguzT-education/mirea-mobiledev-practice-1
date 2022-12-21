package io.github.tuguzt.mirea.todolist.viewmodel.task

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddNewTaskViewModel @Inject constructor() : ViewModel() {
    private var _state by mutableStateOf(AddNewTaskScreenState())
    val state get() = _state

    var taskName: String
        get() = state.name
        set(name) {
            _state = state.copy(name = name)
        }

    var taskContent: String
        get() = state.content
        set(content) {
            _state = state.copy(content = content)
        }

    fun canAdd(): Boolean = taskName.isNotEmpty()

    fun addNewTask() {
        TODO("bind with actual data")
    }
}

@Immutable
data class AddNewTaskScreenState(
    val name: String = "",
    val content: String = "",
)
