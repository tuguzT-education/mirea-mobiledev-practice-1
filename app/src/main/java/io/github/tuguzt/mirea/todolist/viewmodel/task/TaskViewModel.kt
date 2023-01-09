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

    private var setupId: Id<Task>? = null

    fun setup(id: Id<Task>) {
        setupId = id

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
                        val taskState = TaskState.Loaded(task)
                        state.value.copy(refreshing = false, taskState = taskState)
                    }
                }
            )
        }
    }

    fun refresh() {
        val id = requireNotNull(setupId)
        setup(id)
    }

    fun changeTaskCompletion() {
        val taskState = state.value.taskState
        check(taskState is TaskState.Loaded)

        val task = taskState.task
        if (task.completed) {
            reopenTask()
        } else {
            closeTask()
        }
    }

    fun deleteTask() {
        val taskState = state.value.taskState
        check(taskState is TaskState.Loaded)
        val taskId = taskState.task.id

        viewModelScope.launch {
            _state.emit(value = state.value.copy(refreshing = true))
            _state.emit(
                value = when (val result = deleteTask.deleteTask(taskId)) {
                    is Result.Error -> {
                        logger.error(result.error) { "Unexpected error" }
                        val message = UserMessage(result.error.kind())
                        val userMessages = state.value.userMessages + message
                        state.value.copy(refreshing = false, userMessages = userMessages)
                    }
                    is Result.Success -> {
                        state.value.copy(refreshing = false, taskState = TaskState.Deleted)
                    }
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
        val taskState = state.value.taskState
        check(taskState is TaskState.Loaded)
        val task = taskState.task

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
                            taskState = TaskState.Loaded(
                                task = task.copy(completed = true),
                            ),
                        )
                    }
                }
            )
        }
    }

    private fun reopenTask() {
        val taskState = state.value.taskState
        check(taskState is TaskState.Loaded)
        val task = taskState.task

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
                            taskState = TaskState.Loaded(
                                task = task.copy(completed = false),
                            ),
                        )
                    }
                }
            )
        }
    }
}

@Immutable
data class TaskScreenState(
    val taskState: TaskState = TaskState.Initial,
    val refreshing: Boolean = true,
    override val userMessages: List<UserMessage<DomainErrorKind>> = listOf(),
) : MessageState<DomainErrorKind>

@Immutable
sealed interface TaskState {
    @Immutable
    object Initial : TaskState

    @Immutable
    data class Loaded(val task: Task) : TaskState

    @Immutable
    object Deleted : TaskState
}
