package io.github.tuguzt.mirea.todolist.viewmodel.project

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tuguzt.mirea.todolist.domain.Result
import io.github.tuguzt.mirea.todolist.domain.usecase.CreateProject
import io.github.tuguzt.mirea.todolist.viewmodel.DomainErrorKind
import io.github.tuguzt.mirea.todolist.viewmodel.MessageState
import io.github.tuguzt.mirea.todolist.viewmodel.UserMessage
import io.github.tuguzt.mirea.todolist.viewmodel.kind
import kotlinx.coroutines.launch
import mu.KotlinLogging
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddNewProjectViewModel @Inject constructor(
    private val createProject: CreateProject,
) : ViewModel() {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    private var _state by mutableStateOf(AddNewProjectScreenState())
    val state get() = _state

    var projectName: String
        get() = state.name
        set(name) {
            _state = state.copy(name = name)
        }

    fun canAdd(): Boolean = projectName.isNotEmpty()

    fun addNewProject() {
        viewModelScope.launch {
            when (val result = createProject.createProject(state.name)) {
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
data class AddNewProjectScreenState(
    val name: String = "",
    override val userMessages: List<UserMessage<DomainErrorKind>> = listOf(),
) : MessageState<DomainErrorKind>
