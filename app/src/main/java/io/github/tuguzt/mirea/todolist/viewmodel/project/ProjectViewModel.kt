package io.github.tuguzt.mirea.todolist.viewmodel.project

import androidx.compose.runtime.Immutable
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _state = MutableStateFlow(ProjectScreenState())
    val state = _state.asStateFlow()

    private var setupId: Id<Project>? = null

    fun setup(id: Id<Project>) {
        setupId = id

        viewModelScope.launch {
            _state.emit(value = state.value.copy(refreshing = true))

            _state.emit(
                value = when (val result = projectById.projectById(id)) {
                    is Result.Error -> {
                        logger.error(result.error) { "Unexpected error" }
                        val message = UserMessage(result.error.kind())
                        val userMessages = state.value.userMessages + message
                        state.value.copy(refreshing = false, userMessages = userMessages)
                    }
                    is Result.Success -> {
                        val project = checkNotNull(result.data)
                        state.value.copy(refreshing = false, project = project)
                    }
                }
            )
        }
    }

    fun refresh() {
        val id = checkNotNull(setupId)
        setup(id)
    }

    fun changeTaskCompletion(task: Task) {
        if (task.completed) {
            reopenTask(task)
        } else {
            closeTask(task)
        }
    }

    fun deleteProject() {
        val project = checkNotNull(state.value.project)

        viewModelScope.launch {
            _state.emit(value = state.value.copy(refreshing = true))

            _state.emit(
                value = when (val result = deleteProject.deleteProject(project.id)) {
                    is Result.Error -> {
                        logger.error(result.error) { "Unexpected error" }
                        val message = UserMessage(result.error.kind())
                        val userMessages = state.value.userMessages + message
                        state.value.copy(refreshing = false, userMessages = userMessages)
                    }
                    is Result.Success -> {
                        state.value.copy(refreshing = false)
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

    private fun closeTask(task: Task) {
        viewModelScope.launch {
            _state.emit(value = state.value.copy(refreshing = true))

            when (val result = closeTask.closeTask(task.id)) {
                is Result.Error -> {
                    logger.error(result.error) { "Unexpected error" }
                    val message = UserMessage(result.error.kind())
                    val userMessages = state.value.userMessages + message
                    _state.emit(
                        value = state.value.copy(
                            refreshing = false,
                            userMessages = userMessages,
                        ),
                    )
                }
                is Result.Success -> refresh()
            }
        }
    }

    private fun reopenTask(task: Task) {
        viewModelScope.launch {
            _state.emit(value = state.value.copy(refreshing = true))

            when (val result = reopenTask.reopenTask(task.id)) {
                is Result.Error -> {
                    logger.error(result.error) { "Unexpected error" }
                    val message = UserMessage(result.error.kind())
                    val userMessages = state.value.userMessages + message
                    _state.emit(
                        value = state.value.copy(
                            refreshing = false,
                            userMessages = userMessages,
                        ),
                    )
                }
                is Result.Success -> refresh()
            }
        }
    }
}

@Immutable
data class ProjectScreenState(
    val project: Project? = null,
    val refreshing: Boolean = true,
    override val userMessages: List<UserMessage<DomainErrorKind>> = listOf(),
) : MessageState<DomainErrorKind>
