package io.github.tuguzt.mirea.todolist.viewmodel.task

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tuguzt.mirea.todolist.domain.Result
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.usecase.CloseTask
import io.github.tuguzt.mirea.todolist.domain.usecase.DeleteTask
import io.github.tuguzt.mirea.todolist.domain.usecase.ReopenTask
import io.github.tuguzt.mirea.todolist.domain.usecase.TaskById
import io.github.tuguzt.mirea.todolist.viewmodel.DomainErrorKind
import io.github.tuguzt.mirea.todolist.viewmodel.MessageState
import io.github.tuguzt.mirea.todolist.viewmodel.UserMessage
import io.github.tuguzt.mirea.todolist.viewmodel.kind
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _state = MutableStateFlow(TaskScreenState())
    val state = _state.asStateFlow()

    fun setup(id: Id<Task>) {
        viewModelScope.launch {
            _state.emit(value = state.value.copy(refreshing = true))

            _state.emit(
                value = when (val result = taskById.taskById(id)) {
                    is Result.Error -> {
                        logger.error(result.error) { "Unexpected error" }
                        val message = UserMessage(result.error.kind())
                        val userMessages = state.value.userMessages + message
                        state.value.copy(refreshing = false, userMessages = userMessages)
                    }
                    is Result.Success -> {
                        val task = checkNotNull(result.data)
                        state.value.copy(refreshing = false, task = task)
                    }
                }
            )
        }
    }

    fun refresh() {
        val task = requireNotNull(state.value.task)
        setup(task.id)
    }

    fun changeTaskCompletion() {
        val task = checkNotNull(state.value.task)
        if (task.completed) {
            reopenTask()
        } else {
            closeTask()
        }
    }

    fun deleteTask() {
        val task = checkNotNull(state.value.task)

        viewModelScope.launch {
            _state.emit(value = state.value.copy(refreshing = true))

            _state.emit(
                value = when (val result = deleteTask.deleteTask(task.id)) {
                    is Result.Error -> {
                        logger.error(result.error) { "Unexpected error" }
                        val message = UserMessage(result.error.kind())
                        val userMessages = state.value.userMessages + message
                        state.value.copy(refreshing = false, userMessages = userMessages)
                    }
                    is Result.Success -> state.value.copy(refreshing = false)
                }
            )
        }
    }

    fun userMessageShown(messageId: UUID) {
        viewModelScope.launch {
            val messages = state.value.userMessages.filterNot { it.id == messageId }
            _state.emit(value = state.value.copy(userMessages = messages))
        }
    }

    private fun closeTask() {
        val task = checkNotNull(state.value.task)

        viewModelScope.launch {
            _state.emit(value = state.value.copy(refreshing = true))

            _state.emit(
                value = when (val result = closeTask.closeTask(task.id)) {
                    is Result.Error -> {
                        logger.error(result.error) { "Unexpected error" }
                        val message = UserMessage(result.error.kind())
                        val userMessages = state.value.userMessages + message
                        state.value.copy(refreshing = false, userMessages = userMessages)
                    }
                    is Result.Success -> {
                        state.value.copy(
                            refreshing = false,
                            task = task.copy(completed = true),
                        )
                    }
                }
            )
        }
    }

    private fun reopenTask() {
        val task = checkNotNull(state.value.task)

        viewModelScope.launch {
            _state.emit(value = state.value.copy(refreshing = true))

            _state.emit(
                value = when (val result = reopenTask.reopenTask(task.id)) {
                    is Result.Error -> {
                        logger.error(result.error) { "Unexpected error" }
                        val message = UserMessage(result.error.kind())
                        val userMessages = state.value.userMessages + message
                        state.value.copy(refreshing = false, userMessages = userMessages)
                    }
                    is Result.Success -> {
                        state.value.copy(
                            refreshing = false,
                            task = task.copy(completed = false),
                        )
                    }
                }
            )
        }
    }
}

@Immutable
data class TaskScreenState(
    val task: Task? = null,
    val refreshing: Boolean = true,
    override val userMessages: List<UserMessage<DomainErrorKind>> = listOf(),
) : MessageState<DomainErrorKind>
