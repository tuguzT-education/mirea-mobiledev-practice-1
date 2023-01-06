package io.github.tuguzt.mirea.todolist.viewmodel.project

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tuguzt.mirea.todolist.domain.Result
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.usecase.CloseTask
import io.github.tuguzt.mirea.todolist.domain.usecase.DeleteProject
import io.github.tuguzt.mirea.todolist.domain.usecase.ProjectById
import io.github.tuguzt.mirea.todolist.domain.usecase.ReopenTask
import io.github.tuguzt.mirea.todolist.viewmodel.DomainErrorKind
import io.github.tuguzt.mirea.todolist.viewmodel.MessageState
import io.github.tuguzt.mirea.todolist.viewmodel.UserMessage
import io.github.tuguzt.mirea.todolist.viewmodel.kind
import kotlinx.coroutines.launch
import mu.KotlinLogging
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectById: ProjectById,
    private val closeTask: CloseTask,
    private val reopenTask: ReopenTask,
    private val deleteProject: DeleteProject,
) : ViewModel() {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    private var _state by mutableStateOf(ProjectScreenState())
    val state get() = _state

    fun setup(id: Id<Project>) {
        _state = state.copy(isRefreshing = true)

        viewModelScope.launch {
            _state = when (val result = projectById.projectById(id)) {
                is Result.Error -> {
                    logger.error(result.error) { "Unexpected error" }
                    val message = UserMessage(result.error.kind())
                    val userMessages = state.userMessages + message
                    state.copy(isRefreshing = false, userMessages = userMessages)
                }
                is Result.Success -> {
                    val project = checkNotNull(result.data)
                    state.copy(isRefreshing = false, project = project)
                }
            }
        }
    }

    fun refresh() {
        val project = checkNotNull(state.project)
        setup(project.id)
    }

    fun changeTaskCompletion(task: Task) {
        if (task.completed) {
            reopenTask(task)
        } else {
            closeTask(task)
        }
    }

    fun deleteProject() {
        val project = checkNotNull(state.project)
        _state = state.copy(isRefreshing = true)

        viewModelScope.launch {
            _state = when (val result = deleteProject.deleteProject(project.id)) {
                is Result.Error -> {
                    logger.error(result.error) { "Unexpected error" }
                    val message = UserMessage(result.error.kind())
                    val userMessages = state.userMessages + message
                    state.copy(isRefreshing = false, userMessages = userMessages)
                }
                is Result.Success -> {
                    state.copy(isRefreshing = false)
                }
            }
        }
    }

    fun userMessageShown(messageId: UUID) {
        val messages = state.userMessages.filterNot { it.id == messageId }
        _state = state.copy(userMessages = messages)
    }

    private fun closeTask(task: Task) {
        _state = state.copy(isRefreshing = true)

        viewModelScope.launch {
            when (val result = closeTask.closeTask(task.id)) {
                is Result.Error -> {
                    logger.error(result.error) { "Unexpected error" }
                    val message = UserMessage(result.error.kind())
                    val userMessages = state.userMessages + message
                    _state = state.copy(isRefreshing = false, userMessages = userMessages)
                }
                is Result.Success -> refresh()
            }
        }
    }

    private fun reopenTask(task: Task) {
        _state = state.copy(isRefreshing = true)

        viewModelScope.launch {
            when (val result = reopenTask.reopenTask(task.id)) {
                is Result.Error -> {
                    logger.error(result.error) { "Unexpected error" }
                    val message = UserMessage(result.error.kind())
                    val userMessages = state.userMessages + message
                    _state = state.copy(isRefreshing = false, userMessages = userMessages)
                }
                is Result.Success -> refresh()
            }
        }
    }
}

@Immutable
data class ProjectScreenState(
    val project: Project? = null,
    val isRefreshing: Boolean = true,
    override val userMessages: List<UserMessage<DomainErrorKind>> = listOf(),
) : MessageState<DomainErrorKind>
