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
import io.github.tuguzt.mirea.todolist.domain.usecase.CloseTask
import io.github.tuguzt.mirea.todolist.domain.usecase.DeleteTask
import io.github.tuguzt.mirea.todolist.domain.usecase.ReopenTask
import io.github.tuguzt.mirea.todolist.domain.usecase.TaskById
import io.github.tuguzt.mirea.todolist.viewmodel.DomainErrorKind
import io.github.tuguzt.mirea.todolist.viewmodel.MessageState
import io.github.tuguzt.mirea.todolist.viewmodel.UserMessage
import io.github.tuguzt.mirea.todolist.viewmodel.kind
import kotlinx.coroutines.launch
import mu.KotlinLogging
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskById: TaskById,
    private val closeTask: CloseTask,
    private val reopenTask: ReopenTask,
    private val deleteTask: DeleteTask,
) : ViewModel() {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    private var _state by mutableStateOf(TaskScreenState())
    val state get() = _state

    fun setup(taskId: String) {
        viewModelScope.launch {
            _state = when (val result = taskById.taskById(taskId)) {
                is Result.Error -> {
                    logger.error(result.error) { "Unexpected error" }
                    val message = UserMessage(result.error.kind())
                    val userMessages = state.userMessages + message
                    state.copy(userMessages = userMessages)
                }
                is Result.Success -> {
                    val task = checkNotNull(result.data)
                    state.copy(task = task, loading = false)
                }
            }
        }
    }

    fun changeTaskCompletion() {
        val task = checkNotNull(state.task)
        if (task.completed) {
            reopenTask()
        } else {
            closeTask()
        }
    }

    fun deleteTask() {
        _state = state.copy(loading = true)
        viewModelScope.launch {
            val task = checkNotNull(state.task)
            when (val result = deleteTask.deleteTask(task.id)) {
                is Result.Error -> {
                    logger.error(result.error) { "Unexpected error" }
                    val message = UserMessage(result.error.kind())
                    val userMessages = state.userMessages + message
                    _state = state.copy(userMessages = userMessages)
                }
                is Result.Success -> Unit
            }
        }
    }

    fun userMessageShown(messageId: UUID) {
        val messages = state.userMessages.filterNot { it.id == messageId }
        _state = state.copy(userMessages = messages)
    }

    private fun closeTask() {
        _state = state.copy(loading = true)
        viewModelScope.launch {
            val task = checkNotNull(state.task)
            _state = when (val result = closeTask.closeTask(task.id)) {
                is Result.Error -> {
                    logger.error(result.error) { "Unexpected error" }
                    val message = UserMessage(result.error.kind())
                    val userMessages = state.userMessages + message
                    state.copy(userMessages = userMessages)
                }
                is Result.Success -> {
                    state.copy(
                        task = task.copy(completed = true),
                        loading = false,
                    )
                }
            }
        }
    }

    private fun reopenTask() {
        _state = state.copy(loading = true)
        viewModelScope.launch {
            val task = checkNotNull(state.task)
            _state = when (val result = reopenTask.reopenTask(task.id)) {
                is Result.Error -> {
                    logger.error(result.error) { "Unexpected error" }
                    val message = UserMessage(result.error.kind())
                    val userMessages = state.userMessages + message
                    state.copy(userMessages = userMessages)
                }
                is Result.Success -> {
                    state.copy(
                        task = task.copy(completed = false),
                        loading = false,
                    )
                }
            }
        }
    }
}

@Immutable
data class TaskScreenState(
    val task: Task? = null,
    val loading: Boolean = true,
    override val userMessages: List<UserMessage<DomainErrorKind>> = listOf(),
) : MessageState<DomainErrorKind>
