package io.github.tuguzt.mirea.todolist.viewmodel.project

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tuguzt.mirea.todolist.domain.Result
import io.github.tuguzt.mirea.todolist.domain.model.CreateProject
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
import io.github.tuguzt.mirea.todolist.domain.usecase.CreateProject as CreateProjectUseCase

@HiltViewModel
class AddNewProjectViewModel @Inject constructor(
    private val createProject: CreateProjectUseCase,
) : ViewModel() {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    private val _state = MutableStateFlow(AddNewProjectScreenState())
    val state = _state.asStateFlow()

    fun setProjectName(name: String) {
        val newProjectState = state.value.newProjectState
        check(newProjectState is NewProjectState.ToBeCreated)

        viewModelScope.launch {
            val newState = state.value.copy(
                newProjectState = newProjectState.copy(
                    create = newProjectState.create.copy(name = name),
                ),
            )
            _state.emit(newState)
        }
    }

    fun canAdd(): Boolean {
        val newProjectState = state.value.newProjectState as? NewProjectState.ToBeCreated
            ?: return false
        return newProjectState.create.name.isNotEmpty()
    }

    fun addNewProject() {
        check(canAdd())

        val newProjectState = state.value.newProjectState
        check(newProjectState is NewProjectState.ToBeCreated)
        val create = newProjectState.create

        viewModelScope.launch {
            _state.emit(value = state.value.copy(refreshing = true))
            _state.emit(
                value = when (val result = createProject.createProject(create)) {
                    is Result.Error -> {
                        logger.error(result.error) { "Unexpected error" }
                        val message = UserMessage(result.error.kind())
                        val userMessages = state.value.userMessages + message
                        state.value.copy(refreshing = false, userMessages = userMessages)
                    }
                    is Result.Success -> {
                        logger.debug { "Project '${create.name}' was created successfully" }
                        state.value.copy(
                            refreshing = false,
                            newProjectState = NewProjectState.Created,
                        )
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
data class AddNewProjectScreenState(
    val newProjectState: NewProjectState = NewProjectState.ToBeCreated(CreateProject(name = "")),
    val refreshing: Boolean = false,
    override val userMessages: List<UserMessage<DomainErrorKind>> = listOf(),
) : MessageState<DomainErrorKind>

@Immutable
sealed interface NewProjectState {
    @Immutable
    data class ToBeCreated(val create: CreateProject) : NewProjectState

    @Immutable
    object Created : NewProjectState
}
