package io.github.tuguzt.mirea.todolist.viewmodel.task

import androidx.compose.runtime.Immutable
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _state = MutableStateFlow(AddNewTaskScreenState())
    val state = _state.asStateFlow()

    fun setup(id: Id<Project>) {
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
                        val create = CreateTask(project = project.id, name = "", content = "")
                        state.value.copy(
                            refreshing = false,
                            newTaskState = NewTaskState.ToBeCreated(create),
                        )
                    }
                }
            )
        }
    }

    fun setTaskName(name: String) {
        val newTaskState = state.value.newTaskState
        check(newTaskState is NewTaskState.ToBeCreated)

        viewModelScope.launch {
            val newState = state.value.copy(
                newTaskState = newTaskState.copy(
                    create = newTaskState.create.copy(name = name),
                ),
            )
            _state.emit(newState)
        }
    }

    fun setTaskContent(content: String) {
        val newTaskState = state.value.newTaskState
        check(newTaskState is NewTaskState.ToBeCreated)

        viewModelScope.launch {
            val newState = state.value.copy(
                newTaskState = newTaskState.copy(
                    create = newTaskState.create.copy(content = content),
                ),
            )
            _state.emit(newState)
        }
    }

    fun canAdd(): Boolean {
        val newTaskState = state.value.newTaskState as? NewTaskState.ToBeCreated
            ?: return false
        return newTaskState.create.name.isNotEmpty()
    }

    fun addNewTask() {
        check(canAdd())

        val newTaskState = state.value.newTaskState
        check(newTaskState is NewTaskState.ToBeCreated)
        val create = newTaskState.create

        viewModelScope.launch {
            _state.emit(value = state.value.copy(refreshing = true))
            _state.emit(
                value = when (val result = createTask.createTask(create)) {
                    is Result.Error -> {
                        logger.error(result.error) { "Unexpected error" }
                        val message = UserMessage(result.error.kind())
                        val userMessages = state.value.userMessages + message
                        state.value.copy(refreshing = false, userMessages = userMessages)
                    }
                    is Result.Success -> {
                        logger.debug { "Task '${create.name}' was created successfully" }
                        state.value.copy(
                            refreshing = false,
                            newTaskState = NewTaskState.Created,
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
data class AddNewTaskScreenState(
    val newTaskState: NewTaskState = NewTaskState.Initial,
    val refreshing: Boolean = true,
    override val userMessages: List<UserMessage<DomainErrorKind>> = listOf(),
) : MessageState<DomainErrorKind>

@Immutable
sealed interface NewTaskState {
    @Immutable
    object Initial : NewTaskState

    @Immutable
    data class ToBeCreated(val create: CreateTask) : NewTaskState

    @Immutable
    object Created : NewTaskState
}
