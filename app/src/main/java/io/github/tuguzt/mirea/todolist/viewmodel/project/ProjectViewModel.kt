package io.github.tuguzt.mirea.todolist.viewmodel.project

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tuguzt.mirea.todolist.domain.Result
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.usecase.*
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
    private val refreshProjectById: RefreshProjectById,
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
            when (val result = projectById.projectById(id)) {
                is Result.Error -> {
                    logger.error(result.error) { "Unexpected error" }
                    val message = UserMessage(result.error.kind())
                    val userMessages = state.value.userMessages + message
                    _state.emit(state.value.copy(userMessages = userMessages))
                }
                is Result.Success -> {
                    result.data.collect { project ->
                        project?.let {
                            val newProjectState = ProjectState.Loaded(project)
                            _state.emit(value = state.value.copy(projectState = newProjectState))
                        }
                    }
                }
            }
        }
    }

    fun refresh() {
        val id = checkNotNull(setupId)

        viewModelScope.launch {
            _state.emit(value = state.value.copy(refreshing = true))
            _state.emit(
                value = when (val result = refreshProjectById.refreshProjectById(id)) {
                    is Result.Error -> {
                        logger.error(result.error) { "Unexpected error" }
                        val message = UserMessage(result.error.kind())
                        val userMessages = state.value.userMessages + message
                        state.value.copy(refreshing = false, userMessages = userMessages)
                    }
                    is Result.Success -> {
                        logger.debug { "Successful refreshing" }
                        state.value.copy(refreshing = false)
                    }
                }
            )
        }
    }

    fun changeTaskCompletion(task: Task) {
        val projectState = state.value.projectState
        check(projectState is ProjectState.Loaded)

        require(projectState.project.tasks.find { it.id == task.id } != null)

        if (task.completed) {
            reopenTask(task)
        } else {
            closeTask(task)
        }
    }

    fun deleteProject() {
        val projectState = state.value.projectState
        check(projectState is ProjectState.Loaded)
        val projectId = projectState.project.id

        viewModelScope.launch {
            _state.emit(value = state.value.copy(refreshing = true))
            _state.emit(
                value = when (val result = deleteProject.deleteProject(projectId)) {
                    is Result.Error -> {
                        logger.error(result.error) { "Unexpected error" }
                        val message = UserMessage(result.error.kind())
                        val userMessages = state.value.userMessages + message
                        state.value.copy(refreshing = false, userMessages = userMessages)
                    }
                    is Result.Success -> {
                        state.value.copy(refreshing = false, projectState = ProjectState.Deleted)
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
            _state.emit(
                value = when (val result = closeTask.closeTask(task.id)) {
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

    private fun reopenTask(task: Task) {
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
                        state.value.copy(refreshing = false)
                    }
                }
            )
        }
    }
}

@Immutable
data class ProjectScreenState(
    val projectState: ProjectState = ProjectState.Initial,
    val refreshing: Boolean = false,
    override val userMessages: List<UserMessage<DomainErrorKind>> = listOf(),
) : MessageState<DomainErrorKind>

@Immutable
sealed interface ProjectState {
    @Immutable
    object Initial : ProjectState

    @Immutable
    data class Loaded(val project: Project) : ProjectState

    @Immutable
    object Deleted : ProjectState
}
