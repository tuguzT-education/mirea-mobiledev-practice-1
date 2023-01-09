package io.github.tuguzt.mirea.todolist.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tuguzt.mirea.todolist.domain.Result
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.model.Task
import io.github.tuguzt.mirea.todolist.domain.usecase.AllProjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.emit(value = state.value.copy(refreshing = true))

            _state.emit(
                value = when (val result = allProjects.allProjects()) {
                    is Result.Error -> {
                        logger.error(result.error) { "Unexpected error" }
                        val message = UserMessage(result.error.kind())
                        val userMessages = state.value.userMessages + message
                        state.value.copy(refreshing = false, userMessages = userMessages)
                    }
                    is Result.Success -> {
                        logger.debug { "Successful refreshing" }
                        state.value.copy(refreshing = false, projects = result.data)
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
}

@Immutable
data class MainScreenState(
    val projects: List<Project>? = null,
    val refreshing: Boolean = true,
    override val userMessages: List<UserMessage<DomainErrorKind>> = listOf(),
) : MessageState<DomainErrorKind> {

    val todoProjects by lazy {
        projects?.filter { project ->
            val tasks = project.tasks
            tasks.isEmpty() || tasks.size > tasks.count(Task::completed)
        }
    }

    val completedProjects by lazy {
        projects?.filter { project ->
            val tasks = project.tasks
            tasks.isNotEmpty() && tasks.size == tasks.count(Task::completed)
        }
    }
}
