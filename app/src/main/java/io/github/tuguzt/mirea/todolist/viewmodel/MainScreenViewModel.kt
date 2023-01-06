package io.github.tuguzt.mirea.todolist.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tuguzt.mirea.todolist.domain.Result
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.usecase.AllProjects
import kotlinx.coroutines.launch
import mu.KotlinLogging
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val allProjects: AllProjects,
) : ViewModel() {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    private var _state by mutableStateOf(MainScreenState())
    val state get() = _state

    init {
        refresh()
    }

    fun refresh() {
        _state = state.copy(isRefreshing = true)
        viewModelScope.launch {
            _state = when (val result = allProjects.allProjects()) {
                is Result.Error -> {
                    logger.error(result.error) { "Unexpected error" }
                    val message = UserMessage(result.error.kind())
                    val userMessages = state.userMessages + message
                    state.copy(isRefreshing = false, userMessages = userMessages)
                }
                is Result.Success -> {
                    logger.debug { "Successful refreshing" }
                    state.copy(isRefreshing = false, projects = result.data)
                }
            }
        }
    }

    fun userMessageShown(messageId: UUID) {
        val messages = state.userMessages.filterNot { it.id == messageId }
        _state = state.copy(userMessages = messages)
    }
}

@Immutable
data class MainScreenState(
    val projects: List<Project> = listOf(),
    val isRefreshing: Boolean = true,
    override val userMessages: List<UserMessage<DomainErrorKind>> = listOf(),
) : MessageState<DomainErrorKind>

val MainScreenState.todoProjects
    get() = projects.filter { project ->
        project.tasks.isEmpty() || project.tasks.size > project.tasks.count(Task::completed)
    }

val MainScreenState.completedProjects
    get() = projects.filter { project ->
        project.tasks.isNotEmpty() && project.tasks.size == project.tasks.count(Task::completed)
    }
