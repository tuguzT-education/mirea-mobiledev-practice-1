package io.github.tuguzt.mirea.todolist.viewmodel.task

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tuguzt.mirea.todolist.domain.Result
import io.github.tuguzt.mirea.todolist.domain.model.CreateTask
import io.github.tuguzt.mirea.todolist.domain.model.Id
import io.github.tuguzt.mirea.todolist.domain.model.Project
import io.github.tuguzt.mirea.todolist.domain.usecase.ProjectById
import io.github.tuguzt.mirea.todolist.viewmodel.DomainErrorKind
import io.github.tuguzt.mirea.todolist.viewmodel.MessageState
import io.github.tuguzt.mirea.todolist.viewmodel.UserMessage
import io.github.tuguzt.mirea.todolist.viewmodel.kind
import kotlinx.coroutines.launch
import mu.KotlinLogging
import java.util.*
import javax.inject.Inject
import io.github.tuguzt.mirea.todolist.domain.usecase.CreateTask as CreateTaskUseCase

@HiltViewModel
class AddNewTaskViewModel @Inject constructor(
    private val projectById: ProjectById,
    private val createTask: CreateTaskUseCase,
) : ViewModel() {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    private var _state by mutableStateOf(AddNewTaskScreenState())
    val state get() = _state

    fun setup(id: Id<Project>) {
        viewModelScope.launch {
            _state = when (val result = projectById.projectById(id)) {
                is Result.Error -> {
                    logger.error(result.error) { "Unexpected error" }
                    val message = UserMessage(result.error.kind())
                    val userMessages = state.userMessages + message
                    state.copy(userMessages = userMessages)
                }
                is Result.Success -> state.copy(project = result.data)
            }
        }
    }

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
        viewModelScope.launch {
            val project = checkNotNull(state.project)
            val create = CreateTask(project.id, state.name, state.content)
            when (val result = createTask.createTask(create)) {
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
}

@Immutable
data class AddNewTaskScreenState(
    val project: Project? = null,
    val name: String = "",
    val content: String = "",
    override val userMessages: List<UserMessage<DomainErrorKind>> = listOf(),
) : MessageState<DomainErrorKind>
